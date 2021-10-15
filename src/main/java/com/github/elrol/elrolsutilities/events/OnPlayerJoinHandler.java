package com.github.elrol.elrolsutilities.events;

import java.util.UUID;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.data.ServerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.ModInfo;

import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OnPlayerJoinHandler {

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide) {
            if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
                return;
            }
            //Main.patreonList.init();
            ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
            UUID uuid = player.getUUID();
            PlayerData data = Main.database.get(uuid);
            data.username = player.getName().getString();
            if (player.getUUID().equals(ModInfo.Constants.ownerUUID)) {
                Main.mcServer.getPlayerList().broadcastToAllExceptTeam(player, new StringTextComponent(ModInfo.getTag() + TextFormatting.GRAY + "Hello Creator " + Methods.getDisplayName(player)));
            } else {
                if(FeatureConfig.welcome_msg_enable.get()) {
                    StringTextComponent text = new StringTextComponent(ModInfo.getTag());
                    String[] raw = FeatureConfig.welcome_msg_text.get().split("\\{player}");
                    String msg = TextUtils.formatString(raw[0]) + (raw.length > 1 ? data.getDisplayName() + TextUtils.formatString(raw[1]) : "");
                    text.append(new StringTextComponent(msg));
                    Main.mcServer.getPlayerList().broadcastToAllExceptTeam(player, text);
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
            if(!data.firstKit){
                Main.kitMap.values().forEach(kit -> {
                    if(!kit.isDefault()) return;
                    kit.give(player);
                    TextUtils.msg(player, Msgs.received_kit(kit.name));
                });
                data.firstKit = true;
            }
            data.lastOnline = Main.mcServer.getNextTickTime();
            boolean creative = player.gameMode.isCreative();
            player.abilities.mayfly = creative || data.enableFly;
            player.abilities.invulnerable = creative || data.godmode;
            player.abilities.flying = data.isFlying;
            player.onUpdateAbilities();
            data.update();
            data.checkPerms();
            ServerData serverdata = Main.serverData;
            player.sendMessage(new StringTextComponent(serverdata.getMotd()), player.getUUID());
        }
    }
}

