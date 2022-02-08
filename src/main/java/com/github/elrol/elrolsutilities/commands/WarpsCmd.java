package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.ServerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class WarpsCmd
extends _CmdBase {
    public WarpsCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
        Logger.log("Warps Command Initiated");
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        Logger.log("Warps Command Registering");
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
            dispatcher.register(Commands.literal(a).executes(this::execute));
            Logger.log("Warps Command Registered");
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        ServerData sdata = Main.serverData;
        if (sdata.getWarpNames().isEmpty()) {
            TextUtils.err(c, Errs.no_warps());
            return 0;
        }
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
        CommandDelay.init(this, c.getSource(), new CommandRunnable(c.getSource()), false);
        return 1;
    }

    private static class CommandRunnable implements Runnable {
        CommandSource source;

        public CommandRunnable(CommandSource source) {
            this.source = source;
        }

        @Override
        public void run() {
            String warps = null;
            for (String string : Main.serverData.getWarpNames()) {
                if (warps == null) {
                    warps = string;
                    continue;
                }
                warps = warps + ", " + string;
            }
            TextUtils.msg(this.source, Msgs.valid_warps(warps));
        }
    }

}

