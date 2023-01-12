package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
                    .then(Commands.argument("player", EntityArgument.player())
                            .executes(c -> other(c, List.of(EntityArgument.getPlayer(c, "player")))))
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
                    Main.textUtils.err(player, Errs.not_enough_funds(otherCost, data.getBal()));
                    return 0;
                }
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(c.getSource(), target, false), false);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
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
                    Main.textUtils.msg(source, Msgs.bal_self.get(Main.textUtils.parseCurrency(data.getBal(), true)));
                } else {
                    Main.textUtils.msg(source, Msgs.bal_other.get(data.getDisplayName(), Main.textUtils.parseCurrency(data.getBal(), true)));
                }
            });
        }
    }

}

