package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.List;

public class MuteCmd
extends _CmdBase {
    public MuteCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("time", IntegerArgumentType.integer(0))
                                        .executes(this::execute))));
        }

        for (String a : this.aliases) {
            if (this.name.isEmpty()) {
                this.name = a;
            }
            Logger.debug("Registering Alias \"" + a + "\" for Command \"" + this.name + "\"");

        }
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        Collection<ServerPlayer> targets;
        int time = IntegerArgumentType.getInteger(c, "time");
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        try {
            targets = EntityArgument.getPlayers(c, "players");
        }
        catch (CommandSyntaxException e) {
            e.printStackTrace();
            return 0;
        }
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            CommandRunnable cm = new CommandRunnable(c.getSource(), targets, time);
            cm.run();
            return 1;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(c.getSource(), targets, time), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        Collection<ServerPlayer> players;
        CommandSourceStack source;
        int min;

        public CommandRunnable(CommandSourceStack source, Collection<ServerPlayer> players, int min) {
            this.players = players;
            this.source = source;
            this.min = min;
        }

        @Override
        public void run() {
            players.forEach(player -> {
                int old = Main.serverData.getMute(player.getUUID());
                if (old > 0) {
                    if (min <= 0) {
                        Main.serverData.unmutePlayer(player);
                        Main.textUtils.msg(source, Msgs.unmuted_player.get(Methods.getDisplayName(player)));
                    } else {
                        Main.textUtils.msg(source, Msgs.muted_player.get(Methods.getDisplayName(player), old + (old > 1 ? " minutes" : " minute"), min + (min > 1 ? " minutes" : " minute")));
                    }
                } else {
                    Main.textUtils.msg(source, Msgs.muted_player.get(Methods.getDisplayName(player), min + (min > 1 ? " minutes" : " minute")));
                }
                Main.serverData.mutePlayer(player, min);
            });
        }
    }

}

