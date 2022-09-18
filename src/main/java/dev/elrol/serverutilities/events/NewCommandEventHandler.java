package dev.elrol.serverutilities.events;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
                CommandContextBuilder<CommandSourceStack> commandCB = pr.getContext();
                String command = commandCB.getNodes().get(0).getNode().getName();
                for(String cmd : FeatureConfig.jailCommands.get()) {
                    if(cmd.equalsIgnoreCase(command)) {
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
                    Main.textUtils.err(player.createCommandSourceStack(), Errs.jailed(data.getJailTime()));
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
                //Main.mcServer.getPlayerList().broadcastMessage(Component.literal(command), ChatType.SYSTEM, UUID.randomUUID());
                Main.mcServer.getCommands().performPrefixedCommand(source, command);
                //ParsedCommandNode<CommandSourceStack> targets = pr.getContext().getNodes().get(1);
                //ParsedCommandNode<CommandSourceStack> message = pr.getContext().getNodes().get(2);
                Main.getLogger().info("Player ran the /msg command");
                event.setCanceled(true);
            }
        }
    }
}
