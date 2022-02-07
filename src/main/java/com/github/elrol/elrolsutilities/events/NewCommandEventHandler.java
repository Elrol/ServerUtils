package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NewCommandEventHandler {

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        ParseResults<CommandSourceStack> pr = event.getParseResults();
        CommandSourceStack source = pr.getContext().getSource();
        ServerPlayer player = null;
        try {
            player = source.getPlayerOrException();
        } catch (CommandSyntaxException e) {
            source = Main.mcServer.createCommandSourceStack();
        }
        if(player != null) {
            IPlayerData data = Main.database.get(player.getUUID());
            if(data.isJailed()) {
                boolean wl = FeatureConfig.jailCommandsWhitelist.get();
                int flag = -1;
                for(String cmd : FeatureConfig.jailCommands.get()) {
                    if(cmd.equalsIgnoreCase(pr.getContext().getRootNode().getName())) {
                        if(wl) {
                            flag = 1;
                        } else {
                            flag = 0;
                        }
                        break;
                    }
                }
                if(flag == -1) {
                    if(wl) {
                        flag = 0;
                    } else {
                        flag = 1;
                    }
                }
                if(flag == 0) {
                    TextUtils.err(player.createCommandSourceStack(), Errs.jailed(Methods.tickToMin(data.getJailTime())));
                    event.setCanceled(true);
                    return;
                }
            }
        }
        if(pr.getContext().getNodes().size() >= 1) {
            ParsedCommandNode<CommandSourceStack> cmd = pr.getContext().getNodes().get(0);
            if (cmd.getNode().getName().equalsIgnoreCase("msg")) {
                ImmutableStringReader reader = pr.getReader();

                String command = reader.getString().replaceFirst("msg", "pm");
                //Main.mcServer.getPlayerList().broadcastMessage(new TextComponent(command), ChatType.SYSTEM, UUID.randomUUID());
                Main.mcServer.getCommands().performCommand(source, command);
                //ParsedCommandNode<CommandSourceStack> targets = pr.getContext().getNodes().get(1);
                //ParsedCommandNode<CommandSourceStack> message = pr.getContext().getNodes().get(2);
                Main.getLogger().info("Player ran the /msg command");
                event.setCanceled(true);
            }
        }
    }
}
