package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FlyCmd
extends _CmdBase {
    public FlyCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(c -> execute(c, EntityArgument.getPlayers(c, "players")))));
        }
    }

    protected int execute(CommandContext<CommandSource> c, Collection<ServerPlayerEntity> target) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(c.getSource(), target), false);
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        return this.execute(c, Collections.singleton(player));
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSource source;
        Collection<ServerPlayerEntity> players;

        public CommandRunnable(CommandSource source, Collection<ServerPlayerEntity> players) {
            this.source = source;
            this.players = players;
        }

        @Override
        public void run() {
            players.forEach(player -> {
                boolean flag = !player.abilities.mayfly;
                if (source.getTextName().equalsIgnoreCase(player.getName().getString())) {
                    TextUtils.msg(source, Msgs.fly_self.get(flag ? "enabled" : "disabled"));
                } else {
                    TextUtils.msg(source, Msgs.fly_other.get(flag ? "enabled" : "disabled", Methods.getDisplayName(player)));
                    TextUtils.msg(player, Msgs.fly.get(flag ? "enabled" : "disabled"));
                }
                if (!flag) {
                    player.abilities.flying = false;
                }
                player.abilities.mayfly = flag;
                player.onUpdateAbilities();
            });
        }
    }

}

