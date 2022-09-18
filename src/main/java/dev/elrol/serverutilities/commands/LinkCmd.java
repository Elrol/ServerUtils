package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class LinkCmd extends _CmdBase {
    public LinkCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
            dispatcher.register((Commands.literal(a)
                    .executes(this::execute)
                    .then(Commands.argument("verification", StringArgumentType.string())
                            .executes(c -> other(c, StringArgumentType.getString(c, "verification"))))));
        }
    }

    protected int other(CommandContext<CommandSourceStack> c, String verification) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException ignored) {
            player = null;
        }

        if(player != null) {
            IPlayerData data = Main.database.get(player.getUUID());
            if (FeatureConfig.enable_economy.get() && cost > 0) {
                if (!data.charge(cost)) {
                    Main.textUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                    return 0;
                }
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(player, verification, false), false);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        String code = Main.textUtils.generateString();
        Main.serverData.minecraftVerifications.put(code, player.getUUID());
        Main.textUtils.msg(player, Msgs.verification.get(code));
        return 1;
    }

    private static class CommandRunnable implements Runnable {
        ServerPlayer player;
        String code;
        boolean self;

        public CommandRunnable(ServerPlayer player, String code, boolean self) {
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
            Main.textUtils.msg(player, Msgs.verified.get(discordName));
        }
    }

}

