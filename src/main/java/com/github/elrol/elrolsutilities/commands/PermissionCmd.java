package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.commands.permission.*;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PermissionCmd extends _CmdBase {
    public PermissionCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(
                        Commands.literal(a)
                                .executes(this::execute)
                                .then(PermissionAdd.register())
                                .then(PermissionCheck.register())
                                .then(PermissionClear.register())
                                .then(PermissionList.register())
                                .then(PermissionRemove.register()));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        try {
            ServerPlayer player = c.getSource().getPlayerOrException();
            if (Methods.hasCooldown(player, this.name)) {
                return 0;
            }
            IPlayerData data = Main.database.get(player.getUUID());
            if (FeatureConfig.enable_economy.get() && this.cost > 0) {
                if (!data.charge(this.cost)) {
                    TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return 0;
                }
            }
            CommandDelay.init(this, player, new CommandRunnable(player, data), false);
        }
        catch (CommandSyntaxException e) {
            TextUtils.msg(c, Msgs.permission.get());
            c.getSource().sendFailure(Component.literal("*"));
        }
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer player;
        IPlayerData data;

        public CommandRunnable(ServerPlayer player, IPlayerData data) {
            this.player = player;
            this.data = data;
        }

        @Override
        public void run() {
            TextUtils.msg(this.player, Msgs.permission.get());
            for (String p : this.data.getPerms()) {
                TextUtils.sendMessage(player.createCommandSourceStack(), player, p);
            }
        }
    }

}

