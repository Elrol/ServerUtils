package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.api.econ.AbstractShop;
import com.github.elrol.elrolsutilities.api.econ.IShopRegistry;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.ClaimBlock;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class BlockEventHandler {

    protected Map<UUID, Location> locationStorage = new HashMap<>();

    @SubscribeEvent
    public void blockBreak(BlockEvent.BreakEvent event) {
        if(!(event.getPlayer() instanceof ServerPlayer player)) return;
        if(player.level.isClientSide) return;
        ClaimBlock claim = new ClaimBlock(player);
        IPlayerData data = Main.database.get(player.getUUID());
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

            AbstractShop shop = Main.shopRegistry.getShop(loc);
            if(!shop.isAdmin())
                data.getShops().remove(loc);
            Main.shopRegistry.removeShop(loc);
            TextUtils.msg(player, Msgs.removed_shop.get(shop.tag()));
        } else {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos otherPos = loc.getBlockPos().relative(dir);
                if(loc.getLevel() == null) continue;
                if(data.getShops() == null) continue;
                if (Main.shopRegistry.exists(new Location(loc.getLevel(), otherPos, 0f, 0f))) {
                    ServerLevel world = Main.mcServer.getLevel(loc.getLevel());
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
            if(event.getEntityLiving() instanceof ServerPlayer player) {
                IPlayerData data = Main.database.get(uuid);
                if (player.getUUID().equals(uuid) || data.isTrusted(player.getUUID())) return;
                TextUtils.err(player, Errs.chunk_claimed(data.getDisplayName()));
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void blockPlace(BlockEvent.EntityPlaceEvent event){
        if(!(event.getEntity() instanceof ServerPlayer player)) return;

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
        Level world = event.getWorld();
        if(world.isClientSide) return;
        List<BlockPos> remove = new ArrayList<>();
        ResourceLocation dim = world.dimension().location();
        event.getAffectedBlocks().forEach(pos -> {
            Location loc = new Location(dim, pos, 0f, 0f);
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
        Level world = event.getWorld();
        if(world.isClientSide) return;
        if(!(event.getPlayer() instanceof ServerPlayer player)) return;

        IPlayerData pdata = Main.database.get(player.getUUID());
        ItemStack hand = player.getMainHandItem();
        IShopRegistry reg = IElrolAPI.getInstance().getShopInit();
        BlockEntity te = world.getBlockEntity(event.getPos());
        Location loc = new Location(world, event.getPos(), 0f, 0f);

        boolean hasPerm = IElrolAPI.getInstance().getPermissionHandler().hasChunkPermission(player, event.getPos());

        if(!hasPerm) {
            event.setCanceled(true);
            return;
        }

        if(hand.getItem().equals(Items.REDSTONE)) {
            Logger.debug("Used Redstone");
            Location signLoc = locationStorage.get(player.getUUID());
            if(signLoc != null) {
                Logger.debug("Sign location found");
                AbstractShop shop = reg.getShop(signLoc);
                if(shop == null) {
                    locationStorage.remove(player.getUUID());
                    event.setCancellationResult(InteractionResult.FAIL);
                    event.setCanceled(true);
                    return;
                }

                if(shop.link(player, signLoc, loc)) TextUtils.msg(player, Msgs.sign_linked.get(shop.tag()));

                locationStorage.remove(player.getUUID());
            } else {
                if(te instanceof SignBlockEntity sign) {
                    Logger.debug("on a sign");
                    AbstractShop shop;
                    if(reg.exists(loc)) {
                        Logger.debug("that is linked to a shop");
                        shop = reg.getShop(loc);
                        TextUtils.msg(player, Msgs.selected_sign.get(shop.tag()));
                        locationStorage.put(player.getUUID(), loc);
                    } else {
                        Logger.debug("that isn't linked to a shop");
                        shop = reg.parseSign(sign);
                        if(shop != null) {
                            Logger.debug("And stored the location");
                            TextUtils.msg(player, Msgs.selected_sign.get(shop.tag()));
                            locationStorage.put(player.getUUID(), loc);
                        }
                    }
                }
            }
        } else if(te instanceof SignBlockEntity) {
            Logger.debug("Clicked sign");
            if(reg.exists(loc)) {
                Logger.debug("with a shop");
                AbstractShop shop = reg.getShop(loc);
                if(shop.isLinked()) {
                    Logger.debug("that is linked");
                    event.setCanceled(shop.useShop(player, loc));
                }
            }
        }

    }

    @SubscribeEvent
    public void pistonPush(PistonEvent.Pre event){
        BlockPos pos = event.getPos();
        BlockPos target = event.getFaceOffsetPos();
        if(!(event.getWorld() instanceof Level world)) return;
        ResourceLocation dim = world.dimension().location();
        ClaimBlock chunkPos = new ClaimBlock(dim, new ChunkPos(pos));
        ClaimBlock targetChunkPos = new ClaimBlock(dim, new ChunkPos(target));
        UUID chunkOwner = Main.serverData.getOwner(chunkPos);
        UUID targetOwner = Main.serverData.getOwner(targetChunkPos);
        if(targetOwner != null) {
            IPlayerData newData = Main.database.get(targetOwner);
            if (chunkOwner != null) {
                if (targetOwner.equals(chunkOwner) || newData.isTrusted(chunkOwner)) {
                    return;
                }
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void entityDamage(LivingDamageEvent event) {
        if(event.getEntityLiving() instanceof ServerPlayer player) {
            IPlayerData data = Main.database.get(player.getUUID());
            if(data.isJailed()) {
                if(FeatureConfig.jailProtection.get() == 1) event.setAmount(0.0f);
                else if(FeatureConfig.jailProtection.get() == 2) event.setCanceled(true);
            }
        }
    }

    public void updateSignBlock(EntityEvent.CanUpdate event) {
    }
}
