package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.api.econ.IShop;
import com.github.elrol.elrolsutilities.api.econ.IShopManager;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.*;
import com.github.elrol.elrolsutilities.libs.ClaimFlagKeys;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockEventHandler {
    private final List<UUID> tempList = new ArrayList<>();

    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent event) {
        if(!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
        if(player.level.isClientSide) return;
        ClaimBlock claim = new ClaimBlock(player);
        PlayerData data = Main.database.get(player.getUUID());
        if(Main.serverData.isClaimed(claim)) {
            UUID uuid = Main.serverData.getOwner(claim);
            if (!(player.getUUID().equals(uuid) || !data.isTrusted(player.getUUID()))) {
                TextUtils.err(player, Errs.chunk_claimed(data.getDisplayName()));
                event.setCanceled(true);
                return;
            }
        }
        Location loc = new Location(player.level.dimension(), event.getPos(), 0f,0f);
        if(Main.shopRegistry.exists(loc)) {
            boolean flag1 = !Main.shopRegistry.getShop(loc).canEdit(player);
            boolean flag2 = player.getMainHandItem().isEmpty() || !player.getMainHandItem().getItem().equals(Items.REDSTONE);
            if (flag1|| flag2) {
                event.setCanceled(true);
                return;
            }

            IShop shop = Main.shopRegistry.getShop(loc);
            if(!shop.isAdmin())
                data.shops.remove(loc);
            Main.shopRegistry.removeShop(loc);
            TextUtils.msg(player, Msgs.removed_shop());
        } else {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos otherPos = loc.getBlockPos().relative(dir);
                if(loc.getWorld() == null) continue;
                if(data.shops == null) continue;
                if (Main.shopRegistry.exists(new Location(loc.getWorld(), otherPos, 0f, 0f))) {
                    ServerWorld world = Main.mcServer.getLevel(loc.getWorld());
                    if(world == null) break;
                    BlockState state = world.getBlockState(otherPos);
                    Block block = state.getBlock();
                    if (block instanceof WallSignBlock) {
                        Direction face = state.getValue(WallSignBlock.FACING);
                        if (dir.equals(face)) {
                            event.setCanceled(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void livingDestroyBlockEvent(LivingDestroyBlockEvent event){
        if(event.getEntityLiving().level.isClientSide) return;
        ClaimBlock claim = new ClaimBlock(event.getEntityLiving());
        if(Main.serverData.isClaimed(claim)) {
            UUID uuid = Main.serverData.getOwner(claim);
            if(event.getEntityLiving() instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity)event.getEntityLiving();
                PlayerData data = Main.database.get(uuid);
                if (player.getUUID().equals(uuid) || data.isTrusted(player.getUUID())) return;
                TextUtils.err(player, Errs.chunk_claimed(data.getDisplayName()));
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void blockPlace(BlockEvent.EntityPlaceEvent event){
        if(!(event.getEntity() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
        if(player.level.isClientSide) return;
        ResourceLocation dim = event.getEntity().level.dimension().location();
        ClaimBlock claim = new ClaimBlock(dim, event.getPos());
        if(Main.serverData.isClaimed(claim) && !Main.serverData.getOwner(claim).equals(event.getEntity().getUUID())) {
            UUID uuid = Main.serverData.getOwner(claim);
            if (player.getUUID().equals(uuid) || Main.database.get(uuid).isTrusted(player.getUUID())) return;
            TextUtils.err(player, Errs.chunk_claimed(Main.database.get(uuid).getDisplayName()));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void explosionEvent(ExplosionEvent.Detonate event){
        World world = event.getWorld();
        if(world.isClientSide) return;
        List<BlockPos> remove = new ArrayList<>();
        ResourceLocation dim = event.getWorld().dimension().location();
        event.getAffectedBlocks().forEach(pos -> {
            Location loc = new Location(event.getWorld().dimension(), pos, 0f, 0f);
            if (Main.serverData.isClaimed(new ClaimBlock(dim, pos))) {
                remove.add(pos);
            } else if(Main.shopRegistry.exists(loc)) {
                remove.add(pos);
            }
        });
        event.getAffectedBlocks().removeAll(remove);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void interactEvent(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        if(world.isClientSide) return;
        if(!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
        PlayerData pdata = Main.database.get(player.getUUID());
        if(pdata.isJailed()) {
            TextUtils.err(player.createCommandSourceStack(), Errs.jailed((int)pdata.getJailTime()));
            event.setCanceled(true);
        }
        ResourceLocation dim = world.dimension().getRegistryName();
        ClaimBlock claim = new ClaimBlock(dim, event.getPos());
        if(Main.serverData.isClaimed(claim) && !Main.serverData.getOwner(claim).equals(player.getUUID())){
            UUID owner = Main.serverData.getOwner(claim);
            PlayerData data = Main.database.get(owner);
            if(!data.getFlag(ClaimFlagKeys.allow_switch) || !data.isTrusted(player.getUUID())) {
                event.setCanceled(true);
                TextUtils.err(player, Errs.chunk_claimed(data.getDisplayName()));
            }
        }
    }

    @SubscribeEvent
    public void enterChunkEvent(PlayerEvent.EnteringChunk event){
        if(!(event.getEntity() instanceof ServerPlayerEntity)) return;
        if(tempList.contains(event.getEntity().getUUID())){
            tempList.remove(event.getEntity().getUUID());
            return;
        }
        ResourceLocation dim = event.getEntity().level.dimension().location();
        ClaimBlock oldPos = new ClaimBlock(dim, new ChunkPos(event.getOldChunkX(), event.getOldChunkZ()));
        ClaimBlock newPos = new ClaimBlock(dim, new ChunkPos(event.getNewChunkX(), event.getNewChunkZ()));
        UUID oldOwner = Main.serverData.getOwner(oldPos);
        UUID newOwner = Main.serverData.getOwner(newPos);
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        if(newOwner != null) {
            PlayerData newData = Main.database.get(newOwner);
            if(!newData.getFlag(ClaimFlagKeys.allow_entry) && !newOwner.equals(player.getUUID()) && !newData.isTrusted(player.getUUID())){
                tempList.add(player.getUUID());
                TextUtils.err(player, Errs.no_entry(Methods.getDisplayName(newOwner)));
                double x = player.blockPosition().getX() + (2 * (event.getOldChunkX() - event.getNewChunkX()));
                double z = player.blockPosition().getZ() + (2 * (event.getOldChunkZ() - event.getNewChunkZ()));
                BlockPos prevLoc = new BlockPos(x, player.blockPosition().getY(), z);
                Methods.teleport(player, new Location(dim, prevLoc, player.yRot, player.xRot));

                return;
            }
            if (oldOwner != null) {
                if (!newOwner.equals(oldOwner)) {
                    if(newOwner.equals(player.getUUID())) {
                        TextUtils.msg(player, Msgs.enter_exit_claim(Methods.getDisplayName(oldOwner) + "'s", "your own"));
                    } else if(oldOwner.equals(player.getUUID())){
                        TextUtils.msg(player, Msgs.enter_exit_claim("your own", Methods.getDisplayName(newOwner) + "'s"));
                    } else {
                        TextUtils.msg(player, Msgs.enter_exit_claim(Methods.getDisplayName(oldOwner) + "'s", Methods.getDisplayName(newOwner) + "'s"));
                    }
                }
            } else {
                if(newOwner.equals(player.getUUID()))
                    TextUtils.msg(player, Msgs.enter_claim("your own"));
                else
                    TextUtils.msg(player, Msgs.enter_claim(Methods.getDisplayName(newOwner)));
            }
        } else {
            if (oldOwner != null) {
                if(oldOwner.equals(player.getUUID()))
                    TextUtils.msg(player, Msgs.exit_claim("your own"));
                else
                    TextUtils.msg(player, Msgs.exit_claim(Methods.getDisplayName(oldOwner)));
            }
        }
    }

    @SubscribeEvent
    public void pistonPush(PistonEvent.Pre event){
        BlockPos pos = event.getPos();
        BlockPos target = event.getFaceOffsetPos();
        if(!(event.getWorld() instanceof World)) return;
        World world = (World) event.getWorld();
        ResourceLocation dim = world.dimension().location();
        ClaimBlock chunkPos = new ClaimBlock(dim, new ChunkPos(pos));
        ClaimBlock targetChunkPos = new ClaimBlock(dim, new ChunkPos(target));
        UUID chunkOwner = Main.serverData.getOwner(chunkPos);
        UUID targetOwner = Main.serverData.getOwner(targetChunkPos);
        if(targetOwner != null) {
            //Main.getLogger().info("Target Owner: " + targetOwner);
            PlayerData newData = Main.database.get(targetOwner);
            if (chunkOwner != null) {
                //Main.getLogger().info("Chunk Owner: " + chunkOwner);
                if (targetOwner.equals(chunkOwner) || newData.isTrusted(chunkOwner)) {
                    return;
                }
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void entityAttack(AttackEntityEvent event) {
        Entity entitySource = event.getEntity();
        if (!(entitySource instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) entitySource;
        ResourceLocation dim = player.level.dimension().location();
        ClaimBlock chunkPos = new ClaimBlock(dim, new ChunkPos(event.getTarget().blockPosition()));
        if(Main.serverData == null) return;
        UUID chunkOwner = Main.serverData.getOwner(chunkPos);
        if (chunkOwner != null) {
            Main.getLogger().info("Chunk Owner: " + chunkOwner);
            PlayerData newData = Main.database.get(chunkOwner);
            if(!(chunkOwner.equals(player.getUUID()) || newData.isTrusted(player.getUUID())))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void entityDamage(LivingDamageEvent event) {
        if(event.getEntityLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntityLiving();
            PlayerData data = Main.database.get(player.getUUID());
            if(data.isJailed()) {
                if(FeatureConfig.jailProtection.get() == 1) event.setAmount(0.0f);
                else if(FeatureConfig.jailProtection.get() == 2) event.setCanceled(true);
            }
        }
    }
}
