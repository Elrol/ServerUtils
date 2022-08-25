package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.Logger;
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

import java.util.List;

public class UntrustCmd
extends _CmdBase {
    public UntrustCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .executes(this::execute)
                        .then(Commands.argument("player", EntityArgument.player()).executes(this::execute)));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        ServerPlayerEntity target = null;
        try {
            target = EntityArgument.getPlayer(c,"player");
        } catch (CommandSyntaxException e) {
            //TextUtils.err(c, Errs.);
            return 0;
        }
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        IPlayerData tdata = Main.database.get(target.getUUID());
        if(data.isTrusted(target.getUUID())) {
            if (FeatureConfig.enable_economy.get() && this.cost > 0) {
                if (!data.charge(this.cost)) {
                    TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return 0;
                }
            }
            CommandDelay.init(this, player, new CommandRunnable(player, target), false);
        } else {
            TextUtils.err(player, Errs.not_trusted(tdata.getDisplayName()));
            return 0;
        }
        return 1;
    }

    private static class CommandRunnable
            implements Runnable {
        ServerPlayerEntity player;
        ServerPlayerEntity target;

        public CommandRunnable(ServerPlayerEntity player, ServerPlayerEntity target) {
            this.player = player;
            this.target = target;
        }

        @Override
        public void run() {
            IPlayerData playerData = Main.database.get(player.getUUID());
            playerData.untrust(target.getUUID());

            IPlayerData targetData = Main.database.get(target.getUUID());
            TextUtils.msg(player, Msgs.untrusted_player.get(targetData.getDisplayName()));
            TextUtils.err(target, Errs.untrusted_by_player(playerData.getDisplayName()));
        }
    }

}
