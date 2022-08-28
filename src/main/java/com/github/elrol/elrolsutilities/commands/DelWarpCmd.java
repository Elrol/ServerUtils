package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
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

public class DelWarpCmd
extends _CmdBase {
    public DelWarpCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .then(Commands.argument("name", StringArgumentType.string())
                                .suggests(ModSuggestions::suggestWarps)
                                .executes(this::execute)));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        String name = StringArgumentType.getString(c, "name");
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        if (name.isEmpty()) {
            TextUtils.err(c, Errs.no_warp_name());
            return 0;
        }
        if (Main.serverData.getWarp(name) == null) {
            TextUtils.err(c, Errs.warp_not_found(name));
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(c.getSource(), name), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSource source;
        String name;

        public CommandRunnable(CommandSource source, String name) {
            this.source = source;
            this.name = name;
        }

        @Override
        public void run() {
            Main.serverData.delWarp(this.name);
            TextUtils.msg(this.source, Msgs.delWarp.get(this.name));
        }
    }

}

