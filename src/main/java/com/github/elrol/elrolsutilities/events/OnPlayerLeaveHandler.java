package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OnPlayerLeaveHandler {
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Logger.debug("Player left the server");
        if(!(event.getPlayer() instanceof ServerPlayer player)) return;
        if (Main.commandDelays.containsKey(player.getUUID())) {
            Main.commandDelays.get(player.getUUID()).cancel();
            Logger.log(player.getName().getString() + " logged out and their delay was canceled.");
        }
        if (player.getUUID().equals(ModInfo.Constants.ownerUUID)) {
            String msg = "Goodbye Creator " + Methods.getDisplayName(player);
            Main.bot.sendInfoMessage(msg);
            Main.mcServer.getPlayerList().broadcastMessage(
                    new TextComponent(ModInfo.getTag() + ChatFormatting.GRAY + msg),
                    ChatType.CHAT,
                    player.getUUID());
        }
        IPlayerData data = Main.database.get(player.getUUID());
        data.setFly(player.getAbilities().mayfly);
        data.setFlying(player.getAbilities().flying);
        data.setGodmode(player.getAbilities().invulnerable);

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

