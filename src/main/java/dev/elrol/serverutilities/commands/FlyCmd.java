package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FlyCmd
extends _CmdBase {
    public FlyCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(c -> execute(c, EntityArgument.getPlayers(c, "players")))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, Collection<ServerPlayer> target) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(c.getSource(), target), false);
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
        return this.execute(c, Collections.singleton(player));
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        Collection<ServerPlayer> players;

        public CommandRunnable(CommandSourceStack source, Collection<ServerPlayer> players) {
            this.source = source;
            this.players = players;
        }

        @Override
        public void run() {
            players.forEach(player -> {
                boolean flag = !player.getAbilities().mayfly;
                if (source.getTextName().equalsIgnoreCase(player.getName().getString())) {
                    Main.textUtils.msg(source, Msgs.fly_self.get(flag ? "enabled" : "disabled"));
                } else {
                    Main.textUtils.msg(source, Msgs.fly_other.get(flag ? "enabled" : "disabled", Methods.getDisplayName(player)));
                    Main.textUtils.msg(player, Msgs.fly.get(flag ? "enabled" : "disabled"));
                }
                if (!flag) {
                    player.getAbilities().flying = false;
                }
                player.getAbilities().mayfly = flag;
                player.onUpdateAbilities();
            });
        }
    }

}

