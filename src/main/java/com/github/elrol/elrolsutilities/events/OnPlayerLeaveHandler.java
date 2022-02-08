package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OnPlayerLeaveHandler {
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Logger.debug("Player left the server");
        if(!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
        if (Main.commandDelays.containsKey(player.getUUID())) {
            Main.commandDelays.get(player.getUUID()).cancel();
            Logger.log(player.getName().getString() + " logged out and their delay was canceled.");
        }
        if (player.getUUID().equals(ModInfo.Constants.ownerUUID)) {
            String msg = "Goodbye Creator " + Methods.getDisplayName(player);
            Main.bot.sendInfoMessage(msg);
            Main.mcServer.getPlayerList().broadcastMessage(
                    new StringTextComponent(ModInfo.getTag() + TextFormatting.GRAY + msg),
                    ChatType.CHAT,
                    player.getUUID());
        }
        IPlayerData data = Main.database.get(player.getUUID());
        data.setFly(player.abilities.mayfly);
        data.setFlying(player.abilities.flying);
        data.setGodmode(player.abilities.invulnerable);

        long time = data.timeTillNextRank();
        if(!data.canRankUp() && data.timeTillNextRank() != 0){
            long t = Main.mcServer.getNextTickTime() - data.timeLastOnline();
            if(data.timeTillNextRank() - t > 0){
                data.setTimeTillNextRank(time - t);
            } else {
                data.setTimeTillNextRank(0);
                data.allowRankUp(true);
            }
        }
        data.update();
        Main.database.save(player.getUUID());
    }
}

