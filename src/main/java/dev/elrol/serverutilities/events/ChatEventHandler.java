package dev.elrol.serverutilities.events;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.libs.text.Errs;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ChatEventHandler {
    static class OldCompat {
        // In a separate class so we can abuse classloading to hide nonexistent (in some versions) classes
        public static void oldOnPlayerChat(ServerChatEvent.Submitted event)
        {
            ChatEventHandler.onPlayerChat(event);
        }
    }

    //TODO: Remove this whole mess when 1.19.2 support is dropped
    public static void registerChatHandler() {
        try {
            if (ObfuscationReflectionHelper.findField(SharedConstants.class, "f_142952_").get(null).equals("1.19.2")) {
                MinecraftForge.EVENT_BUS.addListener(OldCompat::oldOnPlayerChat);
            } else {
                MinecraftForge.EVENT_BUS.addListener(ChatEventHandler::onPlayerChat);
            }
        }
        catch (IllegalAccessException ignored)
        {
            Main.getLogger().error("Failed to complete version check, 1.19.2 users will receive chat spam in preference over crashing");
            MinecraftForge.EVENT_BUS.addListener(ChatEventHandler::onPlayerChat);
        }
    }

    public static void onPlayerChat(ServerChatEvent event) {
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

