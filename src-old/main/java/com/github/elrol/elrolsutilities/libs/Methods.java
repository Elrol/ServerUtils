package com.github.elrol.elrolsutilities.libs;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.config.Configs;
import com.github.elrol.elrolsutilities.data.CommandCooldown;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.events.ChunkHandler;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.items.IItemHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Methods {

    public static String getTime() {
        return formatTime(LocalDateTime.now());
    }

    public static String formatTime(Object obj) {
        SimpleDateFormat format = new SimpleDateFormat("MMM d - h:mm a");
        return format.format(obj);
    }

    public static long minToTicks(int min) {
        return 72000L * (long)min;
    }

    public static String posToString(BlockPos pos) {
        return "" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }

    public static BlockPos stringToPos(String string) {
        String[] split = string.split(", ");
        try {
            return new BlockPos(
                    Integer.parseInt(split[0]),
                    Integer.parseInt(split[1]),
                    Integer.parseInt(split[2])
            );
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void takeFromChest(IItemHandler chest, List<ItemStack> items) {
        for(ItemStack item : items) {
            int qty = item.getCount();
            for(int i = 0; i < chest.getSlots(); i++) {
                ItemStack stack = chest.extractItem(i, qty, true);
                if(stack.sameItem(item)) {
                    if(stack.getCount() >= qty) {
                        chest.extractItem(i, qty, false);
                        qty = 0;
                    } else {
                        qty -= stack.getCount();
                        chest.extractItem(i, stack.getCount(), false);
                    }
                    if(qty == 0) break;
                }
            }
        }
    }

    public static boolean canAllItemsFit(IItemHandler chest, List<ItemStack> items) {
        List<ItemStack> leftovers = addItemsToChest(chest, items, true);
        for(ItemStack stack : leftovers) {
            if(!stack.isEmpty()) return false;
        }
        return true;
    }

    public static List<ItemStack> addItemsToChest(IItemHandler chest, List<ItemStack> items, boolean test) {
        ArrayList<ItemStack> extraItems = new ArrayList<>();
        for (ItemStack item : items) {
            ItemStack leftOver = item.copy();
            for(int s = 0; s < chest.getSlots(); s++) {
                leftOver = chest.insertItem(s, leftOver, test);
                if(leftOver.isEmpty()) break;
            }
            if(!leftOver.isEmpty()) extraItems.add(leftOver);
        }
        return extraItems;
    }

    public static ItemStack addItemToChest(IItemHandler chest, ItemStack stack, boolean test) {
        int qty = stack.getCount();
        for(int i = 0; i < chest.getSlots(); i++) {
            chest.insertItem(i, stack, test);
        }
        if(qty == 0) return ItemStack.EMPTY;
        stack.setCount(stack.getCount() - qty);
        if(qty > 0) return stack;
        Main.getLogger().error("Methods#addItemToChest[Qty: " + qty + "]");
        return ItemStack.EMPTY;
    }

    public static File getWorldDir(String worldName) {
        return Main.mcServer.isDedicatedServer() ? new File("./" + worldName) : new File("./saves/" + worldName);
    }

    public static boolean hasCooldown(CommandSource source, String name){
        try {
            ServerPlayerEntity player = source.getPlayerOrException();
            return hasCooldown(player, name);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    public static boolean hasCooldown(ServerPlayerEntity player, String name) {
        if (Main.commandCooldowns.containsKey(player.getUUID())) {
            Map<String, CommandCooldown> map = Main.commandCooldowns.get(player.getUUID());
            for (Map.Entry<String, CommandCooldown> entry : map.entrySet()) {
                if (!entry.getKey().equalsIgnoreCase(name)) continue;
                TextUtils.err(player, Errs.cooldown(name, "" + entry.getValue().seconds));
                return true;
            }
        }
        return false;
    }

    public static void teleport(ServerPlayerEntity player, Location loc, boolean track) {
        if(track) {
            IPlayerData data = Main.database.get(player.getUUID());
            data.setPrevLoc(Methods.getPlayerLocation(player));
            data.save();
        }
        ServerWorld world = Main.mcServer.getLevel(loc.getWorld());
        if(world == null) {
            System.out.println("[Methods:123] World was null");
            return;
        }
        teleport(player, world, (double)loc.x() + 0.5D, loc.y(), (double)loc.z() + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static boolean teleport(ServerPlayerEntity player, Location loc, Location newLoc) {
        BlockPos pos = loc.getBlockPos();
        Vector3d playerPos = player.position();
        BlockPos blockPos = new BlockPos(Math.floor(playerPos.x), Math.ceil(playerPos.y), Math.floor(playerPos.z));
        if(pos.equals(blockPos)){

            teleport(player, newLoc);
            return true;
        } else {
            TextUtils.msg(player, Errs.player_moved());
            return false;
        }
    }

    public static void teleport(ServerPlayerEntity player, Location loc) {
        teleport(player, loc, true);
    }

    private static void teleport(Entity entityIn, ServerWorld worldIn, double x, double y, double z, float yaw, float pitch) {
        BlockPos blockpos = new BlockPos(x, y, z);
        if (World.isInSpawnableBounds(blockpos)) {
            if (entityIn instanceof ServerPlayerEntity) {
                ChunkPos chunkpos = new ChunkPos(new BlockPos(x, y, z));
                worldIn.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, entityIn.getId());
                entityIn.stopRiding();
                if (((ServerPlayerEntity)entityIn).isSleeping()) {
                    ((ServerPlayerEntity)entityIn).stopSleepInBed(true, true);
                }

                if (worldIn == entityIn.level) {
                    ((ServerPlayerEntity)entityIn).connection.teleport(x, y, z, yaw, pitch, Collections.singleton(SPlayerPositionLookPacket.Flags.X));
                } else {
                    ((ServerPlayerEntity)entityIn).teleportTo(worldIn, x, y, z, yaw, pitch);
                }

                entityIn.setYHeadRot(yaw);
            } else {
                float f1 = MathHelper.wrapDegrees(yaw);
                float f = MathHelper.wrapDegrees(pitch);
                f = MathHelper.clamp(f, -90.0F, 90.0F);
                if (worldIn == entityIn.level) {
                    entityIn.moveTo(x, y, z, f1, f);
                    entityIn.setYHeadRot(f1);
                } else {
                    entityIn.unRide();
                    Entity entity = entityIn;
                    entityIn = entityIn.getType().create(worldIn);
                    if (entityIn == null) {
                        return;
                    }

                    entityIn.restoreFrom(entity);
                    entityIn.moveTo(x, y, z, f1, f);
                    entityIn.setYHeadRot(f1);
                    worldIn.addFromAnotherDimension(entityIn);
                }
            }

            if (!(entityIn instanceof LivingEntity) || !((LivingEntity)entityIn).isFallFlying()) {
                entityIn.setDeltaMovement(entityIn.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                entityIn.setOnGround(true);
            }

            if (entityIn instanceof CreatureEntity) {
                ((CreatureEntity)entityIn).getNavigation().stop();
            }

        }
    }

    public static Location getPlayerLocation(PlayerEntity player) {
        Vector3d pos = player.position().add(0.0, 0.5, 0.0);
        return new Location(player.level.dimension(), new BlockPos(pos), player.yRot, player.xRot);
    }

    public static ChunkPos getChunk(PlayerEntity player){
        return new ChunkPos(new BlockPos(player.position()));
    }

    public static void randomTeleport(ServerPlayerEntity player, int min, int max) {
        Location randLoc = null;
        Random rand = new Random();
        for (int a = 0; a < 200; ++a) {
            Logger.log("Log1");
            BlockPos tpPos = null;
            int x = (rand.nextBoolean() ? 1 : -1) * (min + rand.nextInt(max - min));
            int z = (rand.nextBoolean() ? 1 : -1) * (min + rand.nextInt(max - min));
            for(int y = 128; y > 0; y--) {
                BlockPos pos = new BlockPos(x, y, z);
                if(player.level.getBlockState(pos).getBlock().equals(Blocks.BEDROCK)) continue;
                if(isValid(player.level, pos)) {
                    tpPos = pos;
                    break;
                }
            }
            if(tpPos == null) continue;
            if(!isValid(player.level, tpPos)) {
                Logger.err("RTP Pos was not valid");
                continue;
            } else {
                Logger.err("06");
                randLoc = new Location(player.level.dimension(), tpPos.offset(0, 1,0), 0.0f, 0.0f);
                Logger.log(player.level.getBlockState(tpPos).getBlock().toString());
                Methods.teleport(player, randLoc);
                TextUtils.msg(player, Msgs.rtp(randLoc.getBlockPos().toString()));
            }
            Logger.log("Log8");
            return;
        }
        TextUtils.err(player, Errs.rtp_error());
    }

    private static boolean isValid(World world, BlockPos pos) {
        boolean flag;
        if(world.getBlockState(pos.offset(0,-1, 0)).getBlock().equals(Blocks.BEDROCK)) return false;
        BlockState state1 = world.getBlockState(pos.offset(0,1,0));
        BlockState state2 = world.getBlockState(pos.offset(0,2,0));

        if(!isValid(state1)) flag = false;
        else if(!isValid(state2)) flag = false;
        else flag = world.getBlockState(pos).canOcclude();
        return flag;
    }

    private static boolean isValid(BlockState state) {
        if(state.getBlock() instanceof IFluidBlock) return false;
        if(state.getBlock() instanceof FlowingFluidBlock) return false;
        return !state.canOcclude();
    }

    public static ServerPlayerEntity getPlayerFromUUID(UUID uuid) {
        return Main.mcServer.getPlayerList().getPlayer(uuid);
    }

    public static GameProfile getPlayerCachedProfile(UUID uuid) {
        return Main.mcServer.getProfileCache().get(uuid);
    }

    public static String getDisplayName(UUID uuid){
        IPlayerData data = Main.database.get(uuid);
        GameProfile cache = Main.mcServer.getProfileCache().get(uuid);
        if(data.getNickname() == null || data.getNickname().isEmpty())
            return cache == null ? "[Null Player]" : cache.getName();
        return TextUtils.formatString(data.getNickname()) + TextFormatting.RESET;
    }

    public static String getDisplayName(PlayerEntity player) {
        return getDisplayName(player.createCommandSource());
    }

    public static String getDisplayName(CommandSource source) {
        String name;
        try {
            ServerPlayerEntity player = source.getPlayerOrException();
            IPlayerData data = Main.database.get(player.getUUID());
            name = data.getNickname() == null || data.getNickname().isEmpty() ? player.getName().getString() : TextUtils.formatString(data.getNickname()) + TextFormatting.RESET;
        } catch (CommandSyntaxException e) {
            name = TextFormatting.GOLD + "<" + TextFormatting.YELLOW + "Server" + TextFormatting.GOLD + ">" + TextFormatting.RESET;
        }
        return name;
    }

    public static long minDiff(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.MINUTES.between(dateTime, now);
    }

    public static int tickToMin(Long tick) {

        Long ticksPerMin = 72000L;
        Long time = tick;
        Logger.debug("Server time: " + time);
        time = time - time % ticksPerMin;
        Logger.debug("Server time rounded: " + time);
        time = time / ticksPerMin;
        Logger.debug("Server time in min: " + time);
        return time.intValue();
    }

    public static UUID getUUIDFromName(String name) {
        for (PlayerData data : Main.database.getDatabase().values()) {
            if (!data.username.equalsIgnoreCase(name)) continue;
            return data.uuid;
        }
        return null;
    }

    public static void reload(){
        Main.permRegistry.load();
        Ranks.load();
        Main.blackLists.load();
        Main.database.loadAll();
        Main.serverData.updateAllPlayers();
        Configs.reload();

        Main.bot.init();
    }

    public static void clearlag(boolean hostile, boolean passive, boolean items){
        AtomicInteger hc = new AtomicInteger((hostile ? 0 : -1));
        AtomicInteger pc = new AtomicInteger((passive ? 0 : -1));
        AtomicInteger ic = new AtomicInteger((items ? 0 : -1));
        Main.mcServer.getAllLevels().forEach(serverWorld -> {
            BlockPos v = clearlag(serverWorld, hostile, passive, items);
            hc.getAndAdd(v.getX());
            pc.getAndAdd(v.getY());
            ic.getAndAdd(v.getZ());
        });
        Main.getLogger().info("Wiped entities: " + hc + ", " + pc + ", " + ic);
        Main.mcServer.getPlayerList().getPlayers().forEach(player -> {
            System.out.println(player);
            TextUtils.msg(player, Msgs.entity_wipe(hc.get(), pc.get(), ic.get()));
        });
    }

    public static BlockPos clearlag(World world, boolean hostile, boolean passive, boolean items){
        int hc = 0, pc = 0, ic = 0;
        for(ChunkPos pos : ChunkHandler.loadedChunks){
            Chunk chunk = world.getChunkSource().getChunk(pos.x, pos.z, false);
            List<Entity> removeList = new ArrayList<>();
            if(chunk == null) continue;
            if(hostile)
                hc += getEntitiesOfType(chunk, MonsterEntity.class, removeList);
            if(passive)
                pc += getEntitiesOfType(chunk, CreatureEntity.class, removeList);
            if(items)
                ic += getEntitiesOfType(chunk, ItemEntity.class, removeList);
            removeList.forEach(Entity::remove);
        }
        return new BlockPos(hc, pc, ic);
    }

    public static <T extends Entity> int getEntitiesOfType(Chunk chunk, Class<? extends T> entityClass, List<T> listToFill) {
        int s = listToFill.size();
        ClassInheritanceMultiMap<Entity>[] entityLists = chunk.getEntitySections();
        for (ClassInheritanceMultiMap<Entity> entityList : entityLists) {
            entityList.find(entityClass).forEach(entity -> {
                if(entity instanceof PlayerEntity) return;
                if(entity instanceof TameableEntity && ((TameableEntity)entity).isTame()) return;
                if(entity instanceof VillagerEntity) return;
                if(!(entityClass.equals(MonsterEntity.class)) && entity instanceof MonsterEntity ) return;
                if(!entity.hasCustomName()) listToFill.add(entity);
            });
        }
        return listToFill.size() - s;
    }
}

