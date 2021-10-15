package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.events.actions.EntityInteractActions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityInteractHandler {
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        boolean cancel = false;
        if (event.getWorld().isClientSide) {
            return;
        }
        Entity entity = event.getTarget();
        if (entity instanceof AbstractMinecartEntity) {
            AbstractMinecartEntity cart = (AbstractMinecartEntity)entity;
            cancel = EntityInteractHandler.inspectHeldItem(event.getPlayer(), cart, Hand.MAIN_HAND);
            if (!cancel) {
                cancel = EntityInteractHandler.inspectHeldItem(event.getPlayer(), cart, Hand.OFF_HAND);
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

