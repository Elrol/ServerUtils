package dev.elrol.serverutilities.events;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.ModInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OnPlayerLeaveHandler {
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Logger.debug("Player left the server");
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        IPlayerData data = Main.database.get(player.getUUID());
        if (Main.commandDelays.containsKey(player.getUUID())) {
            Main.commandDelays.get(player.getUUID()).cancel();
            Logger.log(player.getName().getString() + " logged out and their delay was canceled.");
        }
        if (player.getUUID().equals(ModInfo.Constants.ownerUUID)) {
            String msg = "Goodbye Creator " + Methods.getDisplayName(player);
            Main.bot.sendInfoMessage(msg);
            Main.mcServer.getPlayerList().broadcastSystemMessage(Component.literal(ModInfo.getTag() + ChatFormatting.GRAY + msg),true);
        } else if(FeatureConfig.goodbye_msg_enable.get()){
            MutableComponent text = Component.literal(ModInfo.getTag());
            String msg = Main.textUtils.formatString(FeatureConfig.goodbye_msg_text.get()
                    .replace("{player}", data.getDisplayName()));
            text.append(msg);
            Main.bot.sendInfoMessage(msg);
            Main.mcServer.getPlayerList().broadcastSystemMessage(text, true);
        }
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

