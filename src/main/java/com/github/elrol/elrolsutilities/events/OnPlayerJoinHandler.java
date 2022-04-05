package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.ServerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class OnPlayerJoinHandler {

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide) {
            if (!(event.getPlayer() instanceof ServerPlayer player)) {
                return;
            }
            UUID uuid = player.getUUID();
            IPlayerData data = Main.database.get(uuid);
            data.setUsername(player.getName().getString());

            Main.patreonList.init();
            data.setPatreon(Main.patreonList.has(uuid));
            String msg;
            if (player.getUUID().equals(ModInfo.Constants.ownerUUID)) {
                msg = "Hello Creator " + Methods.getDisplayName(player);
                Main.bot.sendInfoMessage(msg);
                Main.mcServer.getPlayerList().broadcastMessage(new TextComponent(ModInfo.getTag() + ChatFormatting.GRAY + msg), ChatType.CHAT, player.getUUID());
            } else {
                if(FeatureConfig.welcome_msg_enable.get()) {
                    TextComponent text = new TextComponent(ModInfo.getTag());
                    msg = TextUtils.formatString(FeatureConfig.welcome_msg_text.get()
                            .replace("{player}", data.getDisplayName()));
                    text.append(msg);
                    Main.bot.sendInfoMessage(msg);
                    Main.mcServer.getPlayerList().broadcastMessage(text, ChatType.CHAT, player.getUUID());
                }
            }
            if(ModInfo.getRawTag().equals("&5[&dJNEM&5]")){
                TextUtils.msg(player, new TextComponent("Thank you for downloading and playing Just Not Enough Mods 2! Feel free to join the Discord via the Main Menu for help/tips!"));
            }
            if(Main.isCheatMode && Main.mcServer.isSingleplayer()){
                Logger.log("Game is in cheatmode");
                if(Main.mcServer.getSingleplayerName().equals(player.getName().getString())){
                    if(!data.getRanks().contains("op")) {
                        data.getRanks().add("op");
                        Logger.log("Player: " + data.getDisplayName() + " is Op");
                    }
                } else {
                    Logger.log("Player: " + player.getName().getString() + " is not the ServerOwner: " + Main.mcServer.getSingleplayerName());
                }
            }
            if(!data.gotFirstKit()){
                Main.kitMap.values().forEach(kit -> {
                    if(!kit.isDefault()) return;
                    kit.give(player);
                    TextUtils.msg(player, Msgs.received_kit(kit.name));
                });
                data.gotFirstKit(true);
            }
            data.setLastOnline(Main.mcServer.getNextTickTime());
            boolean creative = player.gameMode.isCreative();
            player.getAbilities().mayfly = creative || data.canFly();
            player.getAbilities().invulnerable = creative || data.hasGodmode();
            player.getAbilities().flying = data.isFlying();
            player.onUpdateAbilities();
            data.update();
            data.checkPerms();
            ServerData serverdata = Main.serverData;
            if(!serverdata.getMotd().isEmpty())
                player.sendMessage(new TextComponent(serverdata.getMotd()), player.getUUID());
        }
    }
}

