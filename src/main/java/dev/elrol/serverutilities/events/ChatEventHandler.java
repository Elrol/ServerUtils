package dev.elrol.serverutilities.events;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChatEventHandler {
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onPlayerChat(ServerChatEvent event) {
        int muteTime;
        ServerPlayer player = event.getPlayer();
        IPlayerData data = Main.database.get(player.getUUID());

        muteTime = Main.serverData.getMute(player.getUUID());
        if (muteTime > 0) {
            Main.textUtils.err(player, Errs.are_muted("" + muteTime + (muteTime > 1 ? " minutes" : " minute")));
            event.setCanceled(true);
        }
        if(data.usingStaffChat()) {
            Main.textUtils.sendToStaff(player.createCommandSourceStack(), event.getMessage());
            event.setCanceled(true);
        } else {
            Component text = Main.textUtils.formatChat(player.getUUID(), event.getMessage());
            if (IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.createCommandSourceStack(), FeatureConfig.link_chat_perm.get()))
                text = ForgeHooks.newChatWithLinks(text.getString());
            if(data.isJailed()) {
                player.sendSystemMessage(text);
                Main.textUtils.sendToStaff(player.createCommandSourceStack(), event.getMessage());
                event.setCanceled(true);
            } else {
                Main.mcServer.getPlayerList().broadcastSystemMessage(text,false);
                event.setCanceled(true);
            }
            Main.bot.sendChatMessage(player, event.getMessage().getString());
        }
    }
}

