package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChatEventHandler {
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onPlayerChat(ServerChatEvent event) {
        int muteTime;
        ServerPlayerEntity player = event.getPlayer();
        PlayerData data = Main.database.get(player.getUUID());
        if (data.custperm != null) {
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                data.custperm = null;
                TextUtils.msg(player, Msgs.custperm_cancel());
                event.setCanceled(true);
                return;
            }
            if (!data.custperm.isEmpty()) {
                String perm = event.getMessage();
                if (perm.contains(" ")) {
                    TextUtils.err(player, Errs.custperm_space(perm));
                } else {
                    Main.permRegistry.add(data.custperm, perm);
                    TextUtils.msg(player, Msgs.custperm_2(data.custperm, perm));
                    data.custperm = null;
                }
                event.setCanceled(true);
                return;
            }
        }

        muteTime = Main.serverData.getMute(player.getUUID());
        if (muteTime > 0) {
            TextUtils.err(player, Errs.are_muted("" + muteTime + (muteTime > 1 ? " minutes" : " minute")));
            event.setCanceled(true);
        }
        if(data.staffChatEnabled) {
            TextUtils.sendToStaff(player.createCommandSourceStack(), event.getMessage());
            event.setCanceled(true);
        } else {
            ITextComponent text = TextUtils.formatChat(player, event.getMessage());
            if (IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.createCommandSourceStack(), FeatureConfig.link_chat_perm.get()))
                text = ForgeHooks.newChatWithLinks(text.getString());
            if(data.isJailed()) {
                player.sendMessage(text, player.getUUID());
                TextUtils.sendToStaff(player.createCommandSourceStack(), event.getMessage());
                event.setCanceled(true);
            } else {
                event.setComponent(text);
            }
        }
    }
}

