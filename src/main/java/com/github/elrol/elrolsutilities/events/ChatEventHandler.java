package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChatEventHandler {
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onPlayerChat(ServerChatEvent event) {
        int muteTime;
        ServerPlayerEntity player = event.getPlayer();
        IPlayerData data = Main.database.get(player.getUUID());

        muteTime = Main.serverData.getMute(player.getUUID());
        if (muteTime > 0) {
            TextUtils.err(player, Errs.are_muted("" + muteTime + (muteTime > 1 ? " minutes" : " minute")));
            event.setCanceled(true);
        }
        if(data.usingStaffChat()) {
            TextUtils.sendToStaff(player.createCommandSourceStack(), event.getMessage());
            event.setCanceled(true);
        } else {
            //ITextComponent text = TextUtils.formatChat(player.getUUID(), event.getComponent());
            ITextComponent text = TextUtils.formatChat(player.getUUID(), event.getMessage());
            if (IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.createCommandSourceStack(), FeatureConfig.link_chat_perm.get()))
                text = ForgeHooks.newChatWithLinks(text.getString());
            if(data.isJailed()) {
                player.sendMessage(text, player.getUUID());
                TextUtils.sendToStaff(player.createCommandSourceStack(), event.getMessage());
                event.setCanceled(true);
            } else {
                event.setComponent(text);
            }
            Main.bot.sendChatMessage(player, event.getMessage());
        }
    }
}

