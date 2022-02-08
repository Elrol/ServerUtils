package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingDropHandler {
    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event) {
        if(event.getEntityLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntityLiving();
            IPlayerData data = Main.database.get(player.getUUID());
            if(data.isJailed()) {
                event.setCanceled(true);
            }
        } else if (event.getEntityLiving().getType().equals(EntityType.IRON_GOLEM)) {
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

