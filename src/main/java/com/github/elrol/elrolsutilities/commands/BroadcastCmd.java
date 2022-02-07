package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.List;

public class BroadcastCmd
extends _CmdBase {

    public BroadcastCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : this.aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("msg", StringArgumentType.greedyString())
                                        .executes(this::execute))));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        Collection<ServerPlayer> players;
        CommandSourceStack source = c.getSource();
        ServerPlayer sender = null;
        String msg = StringArgumentType.getString(c, "msg");

        try {
            players = EntityArgument.getPlayers(c, "players");
        } catch (CommandSyntaxException e) {
            return 0;
        }

        try {
            sender = source.getPlayerOrException();
        } catch (CommandSyntaxException ignored) {}

        IPlayerData senderData = (sender == null ? null : Main.database.get(sender.getUUID()));
        if (players.size() > 1 && !IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, "*")) {
            TextUtils.err(c, Errs.cant_select_mulit());
            return 0;
        }
        players.forEach(player -> {
            IPlayerData data = Main.database.get(player.getUUID());
            if (data.msgDisabled()) {
                TextUtils.msg(c, Errs.disabled_msg(Methods.getDisplayName(player)));
                return;
            }
            if (FeatureConfig.enable_economy.get() && this.cost > 0 && senderData != null) {
                if (!senderData.charge(this.cost)) {
                    TextUtils.err(c, Errs.not_enough_funds(this.cost, data.getBal()));
                    return;
                }
            }
            CommandDelay.init(this, player, new CommandRunnable(source, player, msg), false);
        });
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        ServerPlayer player;
        String msg;

        public CommandRunnable(CommandSourceStack source, ServerPlayer player, String msg) {
            this.source = source;
            this.player = player;
            this.msg = msg;
        }

        @Override
        public void run() {
            TextUtils.sendMessage(this.source, this.player, this.msg);
        }
    }

}

