package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BalCmd
extends _CmdBase {
    public BalCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
            dispatcher.register((Commands.literal(a)
                    .executes(this::execute))
                    .then(Commands.argument("players", EntityArgument.players())
                            .executes(c -> other(c, EntityArgument.getPlayers(c, "players")))));
        }
    }

    protected int other(CommandContext<CommandSourceStack> c, Collection<ServerPlayer> target) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException ignored) {
            player = null;
        }

        if(player != null) {
            IPlayerData data = Main.database.get(player.getUUID());
            int otherCost = CommandConfig.bal_other_cost.get();
            if (FeatureConfig.enable_economy.get() && otherCost > 0) {
                if (!data.charge(otherCost)) {
                    TextUtils.err(player, Errs.not_enough_funds(otherCost, data.getBal()));
                    return 0;
                }
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(c.getSource(), target, false), false);
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                TextUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(c.getSource(), Collections.singleton(player), true), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        Collection<ServerPlayer> players;
        boolean self;

        public CommandRunnable(CommandSourceStack source, Collection<ServerPlayer> players, boolean self) {
            this.source = source;
            this.players = players;
            this.self = self;
        }

        @Override
        public void run() {
            players.forEach(player -> {
                IPlayerData data = Main.database.get(player.getUUID());
                if(self){
                    TextUtils.msg(source, Msgs.bal_self(TextUtils.parseCurrency(data.getBal(), true)));
                } else {
                    TextUtils.msg(source, Msgs.bal_other(data.getDisplayName(), TextUtils.parseCurrency(data.getBal(), true)));
                }
            });
        }
    }

}

