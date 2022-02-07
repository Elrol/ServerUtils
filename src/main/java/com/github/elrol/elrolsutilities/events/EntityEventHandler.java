package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.data.ClaimBlock;
import com.github.elrol.elrolsutilities.events.actions.EntityInteractActions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class EntityEventHandler {

    @SubscribeEvent
    public void entityAttack(AttackEntityEvent event) {
        Entity entitySource = event.getEntity();
        if (!(entitySource instanceof ServerPlayer player)) return;
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
        if(!(event.getPlayer() instanceof ServerPlayer player)) return;
        Entity entity = event.getTarget();
        if (entity instanceof AbstractMinecart cart) {
            cancel = EntityEventHandler.inspectHeldItem(player, cart, InteractionHand.MAIN_HAND);
            if (!cancel) {
                cancel = EntityEventHandler.inspectHeldItem(player, cart, InteractionHand.OFF_HAND);
            }
        }
        if (cancel) {
            event.setCanceled(true);
        }
    }

    private static boolean inspectHeldItem(ServerPlayer player, AbstractMinecart cart, InteractionHand hand) {
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

