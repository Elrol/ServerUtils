package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class SetHomeCmd
extends _CmdBase {
    public SetHomeCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .executes(this::execute)
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(c -> execute(c, StringArgumentType.getString(c, "name")))
                        )
                );
        }
    }

    protected int execute(CommandContext<CommandSource> c, String name) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, name)) {
            return 0;
        }
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        PlayerData data = Main.database.get(player.getUUID());
        if (!data.homes.containsKey(name) && data.homes.size() >= data.maxHomes && !data.hasPerm("*")) {
            TextUtils.err(c, Errs.max_home());
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, data, name), false);
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        return this.execute(c, "Home");
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity player;
        PlayerData data;
        String home;

        public CommandRunnable(ServerPlayerEntity player, PlayerData data, String home) {
            this.player = player;
            this.data = data;
            this.home = home;
        }

        @Override
        public void run() {
            this.data.addHome(this.home, Methods.getPlayerLocation(this.player));
            TextUtils.msg(this.player, Msgs.set_home(this.home));
        }
    }

}

