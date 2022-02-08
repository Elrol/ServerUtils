package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PayCmd extends _CmdBase {
    public PayCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(0.0f))
                                        .executes(this::pay))));
        }
    }

    protected int pay(CommandContext<CommandSource> c) {
        ServerPlayerEntity sender;
        ServerPlayerEntity target;
        float amount = FloatArgumentType.getFloat(c, "amount");
        try {
            sender = c.getSource().getPlayerOrException();
            target = EntityArgument.getPlayer(c, "player");
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(sender.getUUID());
        if (data.getBal() < amount) {
            TextUtils.err(sender, Errs.not_enough_funds(cost, data.getBal()));
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                TextUtils.err(sender, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(sender, target, amount), false);
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        String[] help = new String[]{"Sends the amount to the player, taking it from your account."};
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity sender;
        ServerPlayerEntity target;
        float amount;

        public CommandRunnable(ServerPlayerEntity sender, ServerPlayerEntity target, float amount) {
            this.sender = sender;
            this.target = target;
            this.amount = amount;
        }

        @Override
        public void run() {
            IPlayerData senderData = Main.database.get(sender.getUUID());
            IPlayerData targetData = Main.database.get(target.getUUID());
            if(senderData.charge(amount)) targetData.pay(amount);
            TextUtils.msg(sender, Msgs.paid_player(targetData.getDisplayName(), TextUtils.parseCurrency(amount, true)));
            TextUtils.msg(target, Msgs.paid_by(senderData.getDisplayName(), TextUtils.parseCurrency(amount, true)));
        }
    }

}

