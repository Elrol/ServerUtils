package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class UnclaimAllCmd extends _CmdBase {
    public UnclaimAllCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(c -> execute(c, EntityArgument.getPlayer(c, "player")))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, ServerPlayer player) {
        ServerPlayer sender = null;
        try {
             sender = c.getSource().getPlayerOrException();
            if(!sender.getUUID().equals(player.getUUID())) {
                IPlayerData data = Main.database.get(sender.getUUID());
                if(!data.canBypass()) {
                    if(data.hasPerm(Main.permRegistry.getPerm("bypass"))) TextUtils.err(c.getSource(), Errs.bypass_not_enabled());
                    else TextUtils.err(c.getSource(), Errs.no_permission());
                    return 0;
                }
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        if(sender != null) {
            IPlayerData data = Main.database.get(sender.getUUID());
            if (FeatureConfig.enable_economy.get() && this.cost > 0) {
                if (!data.charge(this.cost)) {
                    TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return 0;
                }
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(c.getSource(), player), false);
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        return execute(c, player);
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        ServerPlayer player;

        public CommandRunnable(CommandSourceStack s, ServerPlayer target) {
            source = s;
            player = target;
        }

        @Override
        public void run() {
            Main.serverData.unclaimAll(player);
            IPlayerData data = Main.database.get(player.getUUID());
            try {
                ServerPlayer p = source.getPlayerOrException();
                if(p.getUUID().equals(player.getUUID())) {
                    TextUtils.msg(source, Msgs.chunks_unclaimed());
                    return;
                }
            } catch (CommandSyntaxException ignored) {}
            TextUtils.msg(source, Msgs.chunks_unclaimed(data.getDisplayName() + "'s"));
        }
    }

}

