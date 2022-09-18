package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.List;

public class MsgCmd
extends _CmdBase {
    public MsgCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("msg", StringArgumentType.greedyString())
                                        .executes(this::execute))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        Collection<ServerPlayer> players;
        CommandSourceStack source = c.getSource();
        ServerPlayer sender = null;
        String msg = StringArgumentType.getString(c, "msg");
        try {
            players = EntityArgument.getPlayers(c, "players");
        } catch (CommandSyntaxException e) {
            return 0;
        }

        try {
            sender = source.getPlayerOrException();
        } catch (CommandSyntaxException ignored) {}

        IPlayerData senderData = (sender == null ? null : Main.database.get(sender.getUUID()));
        if (players.size() > 1 && !IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, "*")) {
            Main.textUtils.err(c, Errs.cant_select_mulit());
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && cost > 0 && senderData != null) {
            if (!senderData.charge(cost)) {
                Main.textUtils.err(c, Errs.not_enough_funds(cost, senderData.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, source, new CommandRunnable(source, players, msg), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        Collection<ServerPlayer> players;
        String msg;

        public CommandRunnable(CommandSourceStack source, Collection<ServerPlayer> players, String msg) {
            this.source = source;
            this.players = players;
            this.msg = msg;
        }

        @Override
        public void run() {
            players.forEach(player -> {
                IPlayerData data = Main.database.get(player.getUUID());
                if (data.msgDisabled()) {
                    Main.textUtils.msg(source, Errs.disabled_msg(Methods.getDisplayName(player)));
                    return;
                }
                Main.textUtils.sendMessage(source, player, msg);
            });
        }
    }

}

