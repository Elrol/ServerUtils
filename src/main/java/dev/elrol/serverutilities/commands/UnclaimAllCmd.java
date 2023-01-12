package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
                dispatcher.register(Commands.literal(a)
                        .executes(this::execute)
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
                    if(data.hasPerm(Main.permRegistry.getPerm("bypass"))) Main.textUtils.err(c.getSource(), Errs.bypass_not_enabled());
                    else Main.textUtils.err(c.getSource(), Errs.no_permission());
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
                    Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return 0;
                }
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(c.getSource(), player), false);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
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
                    Main.textUtils.msg(source, Msgs.chunks_unclaimed.get());
                    return;
                }
            } catch (CommandSyntaxException ignored) {}
            Main.textUtils.msg(source, Msgs.chunks_unclaimed_other.get(data.getDisplayName()));
        }
    }

}

