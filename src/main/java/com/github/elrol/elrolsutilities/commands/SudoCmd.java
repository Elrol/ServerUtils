package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class SudoCmd extends _CmdBase {
    public SudoCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(c -> this.execute(c, c.getSource(), EntityArgument.getPlayer(c, "player"), ""))
                                .then(Commands.argument("cmd", StringArgumentType.greedyString())
                                        .executes(c -> this.execute(c, c.getSource(), EntityArgument.getPlayer(c, "player"),
                                                StringArgumentType.getString(c, "cmd"))))

                        ));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, CommandSourceStack source, ServerPlayer target, String cmd) {
        ServerPlayer player;
        if(cmd.isEmpty()) return 0;
        try {
            player = c.getSource().getPlayerOrException();
            IPlayerData data = Main.database.get(player.getUUID());
            if (FeatureConfig.enable_economy.get() && this.cost > 0) {
                if (!data.charge(this.cost)) {
                    TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return 0;
                }
            }
        } catch (CommandSyntaxException e) {
            new CommandRunnable(source, target, cmd).run();
            return 1;
        }

        CommandDelay.init(this, player, new CommandRunnable(source, target, cmd), false);
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        return this.execute(c, c.getSource(), player, "");
    }

    private static class CommandRunnable implements Runnable {
        ServerPlayer player;
        CommandSourceStack source;
        String cmd;

        public CommandRunnable(CommandSourceStack source, ServerPlayer target, String cmd) {
            this.player = target;
            this.source = source;
            this.cmd = cmd;
        }

        @Override
        public void run() {
            if(cmd.startsWith("c:")) Main.mcServer.getPlayerList().broadcastMessage(TextUtils.formatChat(player.getUUID(),cmd.substring(2)), ChatType.CHAT, player.getUUID());
            else Main.mcServer.getCommands().performCommand(player.createCommandSourceStack(), cmd);
        }
    }

}

