package dev.elrol.serverutilities.events;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.libs.Logger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingDropHandler {
    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
        if(event.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer)event.getEntity();
            IPlayerData data = Main.database.get(player.getUUID());
            if(data.isJailed()) {
                event.setCanceled(true);
            }
        } else if (event.getEntity().getType().equals(EntityType.IRON_GOLEM)) {
            int level = event.getLootingLevel();
            for (ItemEntity item : event.getDrops()) {
                ItemStack stack = item.getItem().copy();
                int count = stack.getCount();
                Logger.debug("Increased the stacksize from " + stack.getCount() + " to " + (count += level * 2));
                stack.setCount(count);
                item.setItem(stack);
            }
        }
    }
}

