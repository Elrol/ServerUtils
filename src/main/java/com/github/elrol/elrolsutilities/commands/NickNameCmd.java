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
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class NickNameCmd
extends _CmdBase {
    public NickNameCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
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
    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(null, player, ""), false);
        return 0;
    }

    protected int execute(CommandContext<CommandSourceStack> c, String nick) {
        ServerPlayer player = null;
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

    protected int execute(CommandContext<CommandSourceStack> c, ServerPlayer player, String nick) {
        CommandDelay.init(this, player, new CommandRunnable(c.getSource(), player, nick), false);
        return 0;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        ServerPlayer player;
        String nick;

        public CommandRunnable(CommandSourceStack source, ServerPlayer player, String nick) {
            this.player = player;
            this.nick = nick;
            this.source = source;
        }

        @Override
        public void run() {
            IPlayerData data = Main.database.get(this.player.getUUID());
            if (this.nick.isEmpty()) {
                data.setNickname("");
                if (this.source == null) {
                    TextUtils.msg(this.player, Msgs.nickname_cleared());
                } else {
                    TextUtils.msg(this.source, Msgs.nickname_cleared());
                }
            } else {
                data.setNickname(nick);
                if (this.source == null) {
                    TextUtils.msg(this.player, Msgs.nickname_set(TextUtils.formatString(this.nick)));
                } else {
                    TextUtils.msg(this.source, Msgs.nickname_set(TextUtils.formatString(this.nick)));
                }
            }
        }
    }

}

