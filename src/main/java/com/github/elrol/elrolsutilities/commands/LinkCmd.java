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

public class LinkCmd extends _CmdBase {
    public LinkCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
            dispatcher.register((Commands.literal(a)
                    .executes(this::execute)
                    .then(Commands.argument("verification", StringArgumentType.string())
                            .executes(c -> other(c, StringArgumentType.getString(c, "verification"))))));
        }
    }

    protected int other(CommandContext<CommandSource> c, String verification) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException ignored) {
            player = null;
        }

        if(player != null) {
            IPlayerData data = Main.database.get(player.getUUID());
            if (FeatureConfig.enable_economy.get() && cost > 0) {
                if (!data.charge(cost)) {
                    TextUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                    return 0;
                }
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(player, verification, false), false);
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
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                TextUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        String code = TextUtils.generateString();
        Main.serverData.minecraftVerifications.put(code, player.getUUID());
        TextUtils.msg(player, Msgs.verification.get(code));
        return 1;
    }

    private static class CommandRunnable implements Runnable {
        ServerPlayerEntity player;
        String code;
        boolean self;

        public CommandRunnable(ServerPlayerEntity player, String code, boolean self) {
            this.player = player;
            this.code = code;
            this.self = self;
        }

        @Override
        public void run() {
            IPlayerData data = Main.database.get(player.getUUID());
            long id = Main.serverData.discordVerifications.get(code);
            data.setDiscordID(id);
            data.save();
            String discordName = Main.bot.getDiscordName(id);
            TextUtils.msg(player, Msgs.verified.get(discordName));
        }
    }

}

