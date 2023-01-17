package dev.elrol.serverutilities.libs;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.data.IRank;
import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.config.Configs;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandCooldown;
import dev.elrol.serverutilities.data.DimensionGamemodes;
import dev.elrol.serverutilities.events.ChunkHandler;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Methods {

    public static void runCommand(CommandSourceStack source, String cmd) {
        Commands manager = Main.mcServer.getCommands();
        manager.performPrefixedCommand(source,cmd);
    }

    public static void runCommand(String cmd){
        runCommand(Main.mcServer.createCommandSourceStack(),cmd);
    }

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

    public static File getLevelDir(String worldName) {
        return Main.mcServer.isDedicatedServer() ? new File("./" + worldName) : new File("./saves/" + worldName);
    }

    public static boolean hasCooldown(CommandSourceStack source, String name){
        try {
            ServerPlayer player = source.getPlayerOrException();
            return hasCooldown(player, name);
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    public static boolean hasCooldown(ServerPlayer player, String name) {
        if (Main.commandCooldowns.containsKey(player.getUUID())) {
            Map<String, CommandCooldown> map = Main.commandCooldowns.get(player.getUUID());
            for (Map.Entry<String, CommandCooldown> entry : map.entrySet()) {
                if (!entry.getKey().equalsIgnoreCase(name)) continue;
                Main.textUtils.err(player, Errs.cooldown(name, "" + entry.getValue().seconds));
                return true;
            }
        }
        return false;
    }

    public static void teleport(ServerPlayer player, Location loc, boolean track) {
        if(track) {
            IPlayerData data = Main.database.get(player.getUUID());
            data.setPrevLoc(new Location(player));
            data.save();
        }
        ServerLevel world = Main.mcServer.getLevel(loc.getLevel());
        if(world == null) {
            System.out.println("[Methods:123] Level was null");
            return;
        }
        teleport(player, world, (double)loc.x() + 0.5D, loc.y(), (double)loc.z() + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static boolean teleport(ServerPlayer player, Location loc, Location newLoc) {
        if(loc.isSamePosition(new Location(player))){
            teleport(player, newLoc);
            return true;
        } else {
            Main.textUtils.msg(player, Errs.player_moved());
            return false;
        }
    }

    public static void teleport(ServerPlayer player, Location loc) {
        teleport(player, loc, true);
    }

    private static void teleport(Entity entityIn, ServerLevel worldIn, double x, double y, double z, float yaw, float pitch) {
        BlockPos blockpos = new BlockPos(x, y, z);
        if (Level.isInSpawnableBounds(blockpos)) {
            if (entityIn instanceof ServerPlayer player) {
                ChunkPos chunkpos = new ChunkPos(new BlockPos(x, y, z));
                worldIn.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, entityIn.getId());
                player.teleportTo(worldIn,x,y,z,yaw,pitch);
                entityIn.setYHeadRot(yaw);
                return;
            } else {
                float f1 = Mth.wrapDegrees(yaw);
                float f = Mth.wrapDegrees(pitch);
                f = Mth.clamp(f, -90.0F, 90.0F);
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
                    worldIn.addDuringTeleport(entityIn);
                }
            }

            if (!(entityIn instanceof LivingEntity) || !((LivingEntity)entityIn).isFallFlying()) {
                entityIn.setDeltaMovement(entityIn.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                entityIn.setOnGround(true);
            }

            if (entityIn instanceof PathfinderMob) {
                ((PathfinderMob)entityIn).getNavigation().stop();
            }

        }
    }

    public static ChunkPos getChunk(ServerPlayer player){
        return new ChunkPos(new BlockPos(player.position()));
    }

    public static void randomTeleport(ServerPlayer player, int min, int max) {
        Location randLoc = null;
        Random rand = new Random();
        for (int a = 0; a < 200; ++a) {
            BlockPos tpPos = null;
            int randX = rand.nextInt(max);
            int x = (rand.nextBoolean() ? 1 : -1) * randX;
            int z = (rand.nextBoolean() ? 1 : -1) * (randX < min ? min + rand.nextInt(max - min) : rand.nextInt(max));
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
                randLoc = new Location(player.level.dimension(), tpPos.offset(0, 1,0), 0.0f, 0.0f);
                Logger.log(player.level.getBlockState(tpPos).getBlock().toString());
                Methods.teleport(player, randLoc);
                Main.textUtils.msg(player, Msgs.rtp.get(randLoc.getBlockPos().toString()));
            }
            return;
        }
        Main.textUtils.err(player, Errs.rtp_error());
    }

    private static boolean isValid(Level world, BlockPos pos) {
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
        if(state.getBlock() instanceof LiquidBlock) return false;
        return !state.canOcclude();
    }

    public static UUID getUUIDFromProfileName(String name) {
        Optional<GameProfile> profile = Main.mcServer.getProfileCache().get(name);
        return profile.map(GameProfile::getId).orElse(null);
    }

    public static ServerPlayer getPlayerFromName(String name) {
        return Main.mcServer.getPlayerList().getPlayerByName(name);
    }

    public static ServerPlayer getPlayerFromUUID(UUID uuid) {
        return Main.mcServer.getPlayerList().getPlayer(uuid);
    }

    public static Optional<GameProfile> getPlayerCachedProfile(UUID uuid) {
        return Main.mcServer.getProfileCache().get(uuid);
    }

    public static String getDisplayName(UUID uuid){
        IPlayerData data = Main.database.get(uuid);
        Optional<GameProfile> cache = getPlayerCachedProfile(uuid);
        if(data.getNickname() == null || data.getNickname().isEmpty())
            return cache.isPresent() ? cache.get().getName() : "[Null Player]";
        return Main.textUtils.formatString(data.getNickname()) + ChatFormatting.RESET;
    }

    public static String getDisplayName(ServerPlayer player) {
        return getDisplayName(player.createCommandSourceStack());
    }

    public static String getDisplayName(CommandSourceStack source) {
        String name;
        try {
            ServerPlayer player = source.getPlayerOrException();
            IPlayerData data = Main.database.get(player.getUUID());
            name = data.getNickname() == null || data.getNickname().isEmpty() ? player.getName().getString() : Main.textUtils.formatString(data.getNickname()) + ChatFormatting.RESET;
        } catch (CommandSyntaxException e) {
            name = ChatFormatting.GOLD + "<" + ChatFormatting.YELLOW + "Server" + ChatFormatting.GOLD + ">" + ChatFormatting.RESET;
        }
        return name;
    }

    public static long minDiff(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.MINUTES.between(dateTime, now);
    }

    public static int tickToMin(Long min) {
        Long ticksPerMin = 72000L;
        long time = min * ticksPerMin;
        Logger.debug("Server time: " + time);
        time = time - time % ticksPerMin;
        Logger.debug("Server time rounded: " + time);
        time = time / ticksPerMin;
        Logger.debug("Server time in min: " + time);
        return (int) time;
    }

    public static UUID getUUIDFromName(String name) {
        for (IPlayerData data : Main.database.getDatabase().values()) {
            if (!data.getUsername().equalsIgnoreCase(name)) continue;
            return data.getUUID();
        }
        return null;
    }

    public static void reload(){
        Main.permRegistry.load();
        Ranks.load();
        Main.blacklists.reload();
        Main.database.load();
        Main.serverData.updateAllPlayers();
        Configs.reload();
        Main.bot.shutdown();
        Main.bot.init();
        Main.dimModes = DimensionGamemodes.load();
        if(FeatureConfig.votingEnabled.get())
            Main.vote.reload();
    }

    public static void clearlag(boolean hostile, boolean passive, boolean items){
        AtomicInteger hc = new AtomicInteger(0);
        AtomicInteger pc = new AtomicInteger(0);
        AtomicInteger ic = new AtomicInteger(0);
        Main.mcServer.getAllLevels().forEach(serverLevel -> {
            BlockPos v = clearlag(serverLevel, hostile, passive, items);
            hc.getAndAdd(v.getX());
            pc.getAndAdd(v.getY());
            ic.getAndAdd(v.getZ());
        });
        Logger.log("Wiped entities: " + hc + ", " + pc + ", " + ic);
        Main.mcServer.getPlayerList().getPlayers().forEach(
                p -> Main.textUtils.msg(p, Msgs.entity_wipe.get(String.valueOf(hc.get()), String.valueOf(pc.get()), String.valueOf(ic.get()))));
    }

    public static BlockPos clearlag(Level world, boolean hostile, boolean passive, boolean items){
        int hc = 0, pc = 0, ic = 0;
        for(ChunkPos pos : ChunkHandler.loadedChunks){
            LevelChunk chunk = world.getChunkSource().getChunk(pos.x, pos.z, false);
            List<Entity> removeList = new ArrayList<>();
            if(chunk == null) continue;
            if(hostile)
                hc += getEntitiesOfType(chunk, Monster.class, removeList);
            if(passive)
                pc += getEntitiesOfType(chunk, PathfinderMob.class, removeList);
            if(items)
                ic += getEntitiesOfType(chunk, ItemEntity.class, removeList);
            removeList.forEach(e -> e.remove(Entity.RemovalReason.KILLED));
        }
        return new BlockPos(hc, pc, ic);
    }

    public static <T extends Entity> int getEntitiesOfType(LevelChunk chunk, Class<? extends T> entityClass, List<T> listToFill) {
        int s = listToFill.size();
        ChunkPos pos = chunk.getPos();
        BlockPos max = new BlockPos(pos.getMaxBlockX(), 255, pos.getMaxBlockZ());
        BlockPos min = new BlockPos(pos.getMinBlockX(), 0, pos.getMinBlockZ());
        List<? extends T> entities = chunk.getLevel().getEntitiesOfClass(entityClass, new AABB(min, max));
        for (T entity : entities) {
            if(entity instanceof ServerPlayer) continue;
            if(entity instanceof TamableAnimal && ((TamableAnimal)entity).isTame()) continue;
            if(entity instanceof Villager) continue;
            if(entityClass.equals(Monster.class) && !(entity instanceof Monster)) continue;
            if(!entity.hasCustomName()) {
                if(entity instanceof ItemEntity item) {
                    ItemStack stack = item.getItem();
                    ResourceLocation itemName = ForgeRegistries.ITEMS.getKey(stack.getItem());
                    assert itemName != null;
                    if(Main.blacklists.clearlag_item_blacklist.contains(itemName.toString()))
                        continue;
                } else {
                    ResourceLocation entityName = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
                    assert entityName != null;
                    if(Main.blacklists.clearlag_entity_blacklist.contains(entityName.toString()))
                        continue;
                }
                listToFill.add(entity);
            }
        }
        return listToFill.size() - s;
    }

    public static boolean canModifyRank(ServerPlayer sender, IRank rank) {
        if(IElrolAPI.getInstance().getPermissionHandler().hasPermission(sender, "*"))
            return true;
        IPlayerData data = Main.database.get(sender.getUUID());
        IRank senderRank = data.getDomRank();
        return rank.getWeight() <= senderRank.getWeight();
    }
}

