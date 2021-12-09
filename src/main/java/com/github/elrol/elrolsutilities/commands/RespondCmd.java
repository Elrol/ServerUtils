package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class RespondCmd
extends _CmdBase {
    public RespondCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .then(Commands.argument("msg", StringArgumentType.greedyString())
                                .executes(this::execute)));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        IPlayerData data;
        ServerPlayerEntity player;
        ServerPlayerEntity source;
        String msg = StringArgumentType.getString(c, "msg");
        try {
            source = c.getSource().getPlayerOrException();
            data = Main.database.get(source.getUUID());
            player = data.getLastMsg();
        }
        catch (CommandSyntaxException e) {
            return 0;
        }
        if (player == null) {
            TextUtils.err(c, Errs.no_responder());
            return 0;
        }
        IPlayerData targetData = Main.database.get(player.getUUID());
        if (targetData.msgDisabled()) {
            TextUtils.err(c, Errs.disabled_msg(Methods.getDisplayName(player)));
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(source, player, msg), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity source;
        ServerPlayerEntity player;
        String msg;

        public CommandRunnable(ServerPlayerEntity source, ServerPlayerEntity player, String msg) {
            this.source = source;
            this.player = player;
            this.msg = msg;
        }

        @Override
        public void run() {
            TextUtils.sendMessage(this.source.createCommandSourceStack(), this.player, this.msg);
        }
    }

}

