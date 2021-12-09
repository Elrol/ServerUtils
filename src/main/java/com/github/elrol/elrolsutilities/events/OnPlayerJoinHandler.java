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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class OnPlayerJoinHandler {

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide) {
            if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
                return;
            }
            ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
            UUID uuid = player.getUUID();
            IPlayerData data = Main.database.get(uuid);
            data.setUsername(player.getName().getString());

            Main.patreonList.init();
            data.setPatreon(Main.patreonList.has(uuid));
            String msg;
            if (player.getUUID().equals(ModInfo.Constants.ownerUUID)) {
                msg = "Hello Creator " + Methods.getDisplayName(player);
                Main.bot.sendInfoMessage(msg);
                Main.mcServer.getPlayerList().broadcastMessage(new StringTextComponent(ModInfo.getTag() + TextFormatting.GRAY + msg), ChatType.CHAT, player.getUUID());
            } else {
                if(FeatureConfig.welcome_msg_enable.get()) {
                    StringTextComponent text = new StringTextComponent(ModInfo.getTag());
                    String[] raw = FeatureConfig.welcome_msg_text.get().split("\\{player}");
                    msg = raw[0] + (raw.length > 1 ? data.getDisplayName() + raw[1] : "");
                    text.append(new StringTextComponent(TextUtils.formatString(msg)));
                    Main.bot.sendInfoMessage(msg);
                    Main.mcServer.getPlayerList().broadcastMessage(text, ChatType.CHAT, player.getUUID());
                }
            }
            if(ModInfo.getRawTag().equals("&5[&dJNEM&5]")){
                TextUtils.msg(player, new StringTextComponent("Thank you for downloading and playing Just Not Enough Mods 2! Feel free to join the Discord via the Main Menu for help/tips!"));
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
            player.abilities.mayfly = creative || data.canFly();
            player.abilities.invulnerable = creative || data.hasGodmode();
            player.abilities.flying = data.isFlying();
            player.onUpdateAbilities();
            data.update();
            data.checkPerms();
            ServerData serverdata = Main.serverData;
            if(!serverdata.getMotd().isEmpty())
                player.sendMessage(new StringTextComponent(serverdata.getMotd()), player.getUUID());
        }
    }
}

