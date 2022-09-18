package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PayCmd extends _CmdBase {
    public PayCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(0.0f))
                                        .executes(this::pay))));
        }
    }

    protected int pay(CommandContext<CommandSourceStack> c) {
        ServerPlayer sender;
        ServerPlayer target;
        float amount = FloatArgumentType.getFloat(c, "amount");
        try {
            sender = c.getSource().getPlayerOrException();
            target = EntityArgument.getPlayer(c, "player");
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(sender.getUUID());
        if (data.getBal() < amount) {
            Main.textUtils.err(sender, Errs.not_enough_funds(cost, data.getBal()));
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                Main.textUtils.err(sender, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(sender, target, amount), false);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{"Sends the amount to the player, taking it from your account."};
        Main.textUtils.msgNoTag(c.getSource(), Main.textUtils.commandHelp(help));
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer sender;
        ServerPlayer target;
        float amount;

        public CommandRunnable(ServerPlayer sender, ServerPlayer target, float amount) {
            this.sender = sender;
            this.target = target;
            this.amount = amount;
        }

        @Override
        public void run() {
            IPlayerData senderData = Main.database.get(sender.getUUID());
            IPlayerData targetData = Main.database.get(target.getUUID());
            if(senderData.charge(amount)) targetData.pay(amount);
            Main.textUtils.msg(sender, Msgs.paid_player.get(targetData.getDisplayName(), Main.textUtils.parseCurrency(amount, true)));
            Main.textUtils.msg(target, Msgs.paid_by.get(senderData.getDisplayName(), Main.textUtils.parseCurrency(amount, true)));
        }
    }

}

