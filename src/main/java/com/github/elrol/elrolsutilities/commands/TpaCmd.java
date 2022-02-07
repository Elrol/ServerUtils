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
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class TpaCmd
extends _CmdBase {
    public TpaCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a).then(Commands.argument("player", EntityArgument.player()).executes(this::execute)));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer target;
        ServerPlayer requester;
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
        ServerPlayer requester;
        ServerPlayer target;

        public CommandRunnable(ServerPlayer requester, ServerPlayer target) {
            this.requester = requester;
            this.target = target;
        }

        @Override
        public void run() {
            TextUtils.msg(this.requester, Msgs.tpa_sent(Methods.getDisplayName(this.target)));
            TextUtils.msg(this.target, Msgs.tpa_received(Methods.getDisplayName(this.requester)));
            new TpRequest(this.requester, this.target, false);
        }
    }

}

