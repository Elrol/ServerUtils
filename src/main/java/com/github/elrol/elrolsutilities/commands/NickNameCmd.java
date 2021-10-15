package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
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

import java.util.List;

public class NickNameCmd
extends _CmdBase {
    public NickNameCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a).executes(this::execute))
                        .then(Commands.argument("nick", StringArgumentType.string())
                                .executes(c -> this.execute(c, StringArgumentType.getString(c, "nick")))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(c -> this.execute(c, EntityArgument.getPlayer(c, "player"), StringArgumentType.getString(c, "nick"))))));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        PlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(null, player, ""), false);
        return 0;
    }

    protected int execute(CommandContext<CommandSource> c, String nick) {
        ServerPlayerEntity player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        CommandDelay.init(this, player, new CommandRunnable(null, player, nick), false);
        return 0;
    }

    protected int execute(CommandContext<CommandSource> c, ServerPlayerEntity player, String nick) {
        CommandDelay.init(this, player, new CommandRunnable(c.getSource(), player, nick), false);
        return 0;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSource source;
        ServerPlayerEntity player;
        String nick;

        public CommandRunnable(CommandSource source, ServerPlayerEntity player, String nick) {
            this.player = player;
            this.nick = nick;
            this.source = source;
        }

        @Override
        public void run() {
            PlayerData data = Main.database.get(this.player.getUUID());
            if (this.nick.isEmpty()) {
                data.nickname = "";
                if (this.source == null) {
                    TextUtils.msg(this.player, Msgs.nickname_cleared());
                } else {
                    TextUtils.msg(this.source, Msgs.nickname_cleared());
                }
            } else {
                data.nickname = this.nick;
                if (this.source == null) {
                    TextUtils.msg(this.player, Msgs.nickname_set(TextUtils.formatString(this.nick)));
                } else {
                    TextUtils.msg(this.source, Msgs.nickname_set(TextUtils.formatString(this.nick)));
                }
            }
        }
    }

}

