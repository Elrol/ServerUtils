package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.data.ClaimBlock;
import com.github.elrol.elrolsutilities.events.actions.EntityInteractActions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class EntityEventHandler {

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
            IPlayerData newData = Main.database.get(chunkOwner);
            if(!(chunkOwner.equals(player.getUUID()) || newData.isTrusted(player.getUUID())))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        boolean cancel = false;
        if (event.getWorld().isClientSide) {
            return;
        }
        Entity entity = event.getTarget();
        if (entity instanceof AbstractMinecartEntity) {
            AbstractMinecartEntity cart = (AbstractMinecartEntity)entity;
            cancel = EntityEventHandler.inspectHeldItem(event.getPlayer(), cart, Hand.MAIN_HAND);
            if (!cancel) {
                cancel = EntityEventHandler.inspectHeldItem(event.getPlayer(), cart, Hand.OFF_HAND);
            }
        }
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static boolean inspectHeldItem(PlayerEntity player, AbstractMinecartEntity cart, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem().equals(Items.NAME_TAG)) {
            if (!stack.hasCustomHoverName()) {
                return false;
            }
            if (EntityInteractActions.nameMinecart(cart, stack.getHoverName())) {
                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                }
                return true;
            }
        }
        return false;
    }
}

