package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.TpRequest;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class TpaHereCmd
extends _CmdBase {
    public TpaHereCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a).then(Commands.argument("player", EntityArgument.player()).executes(this::execute)));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity target;
        ServerPlayerEntity requester;
        try {
            target = EntityArgument.getPlayer(c, "player");
            requester = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            e.printStackTrace();
            return 0;
        }
        if (Methods.hasCooldown(requester, this.name)) {
            return 0;
        }
        IPlayerData data = Main.database.get(requester.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(requester, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, requester, new CommandRunnable(requester, target), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity requester;
        ServerPlayerEntity target;

        public CommandRunnable(ServerPlayerEntity requester, ServerPlayerEntity target) {
            this.requester = requester;
            this.target = target;
        }

        @Override
        public void run() {
            TextUtils.msg(this.requester, Msgs.tpa_here_sent(Methods.getDisplayName(this.target)));
            TextUtils.msg(this.target, Msgs.tpa_here_received(Methods.getDisplayName(this.requester)));
            new TpRequest(this.requester.getUUID(), this.target.getUUID(), true);
        }
    }

}

