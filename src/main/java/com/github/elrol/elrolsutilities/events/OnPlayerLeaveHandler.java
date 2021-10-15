package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.ModInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OnPlayerLeaveHandler {
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Logger.debug("Player left the server");
        PlayerEntity player = event.getPlayer();
        if (Main.commandDelays.containsKey(player.getUUID())) {
            Main.commandDelays.get(player.getUUID()).cancel();
            Logger.log(player.getName().getString() + " logged out and their delay was canceled.");
        }
        if (player.getUUID().equals(ModInfo.Constants.ownerUUID)) {
            Main.mcServer.getPlayerList().broadcastToAllExceptTeam(player, new StringTextComponent(ModInfo.getTag() + TextFormatting.GRAY + "Goodbye Creator " + Methods.getDisplayName(player)));
        }
        PlayerData data = Main.database.get(player.getUUID());
        data.enableFly = player.abilities.mayfly;
        data.isFlying = player.abilities.flying;
        data.godmode = player.abilities.invulnerable;
        if(!data.canRankUp && data.nextRank != 0){
            long t = Main.mcServer.getNextTickTime() - data.lastOnline;
            if(data.nextRank - t > 0){
                data.nextRank -= t;
            } else {
                data.nextRank = 0;
                data.canRankUp = true;
            }
        }
        data.update();
        Main.database.save(player.getUUID());
    }
}

