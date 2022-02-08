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
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.List;

public class MsgCmd
extends _CmdBase {
    public MsgCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("msg", StringArgumentType.greedyString())
                                        .executes(this::execute))));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        Collection<ServerPlayerEntity> players;
        CommandSource source = c.getSource();
        ServerPlayerEntity sender = null;
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
        if (FeatureConfig.enable_economy.get() && cost > 0 && senderData != null) {
            if (!senderData.charge(cost)) {
                TextUtils.err(c, Errs.not_enough_funds(cost, senderData.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, source, new CommandRunnable(source, players, msg), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSource source;
        Collection<ServerPlayerEntity> players;
        String msg;

        public CommandRunnable(CommandSource source, Collection<ServerPlayerEntity> players, String msg) {
            this.source = source;
            this.players = players;
            this.msg = msg;
        }

        @Override
        public void run() {
            players.forEach(player -> {
                IPlayerData data = Main.database.get(player.getUUID());
                if (data.msgDisabled()) {
                    TextUtils.msg(source, Errs.disabled_msg(Methods.getDisplayName(player)));
                    return;
                }
                TextUtils.sendMessage(source, player, msg);
            });
        }
    }

}

