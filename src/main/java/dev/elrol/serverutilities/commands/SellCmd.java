package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class SellCmd extends _CmdBase {
    public SellCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a).executes(this::execute)));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer sender;
        try {
            sender = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(sender.getUUID());
        if (FeatureConfig.enable_economy.get() && data.getBal() >= cost) {
            if (!data.charge(cost)) {
                Main.textUtils.err(sender, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        if(sender.getMainHandItem().isEmpty()) {
            Main.textUtils.err(sender, Errs.empty_hand());
            return 0;
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(sender, -1), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer sender;
        int qty;

        public CommandRunnable(ServerPlayer sender, int qty) {
            this.sender = sender;
            this.qty = qty;
        }

        @Override
        public void run() {
            IPlayerData senderData = Main.database.get(sender.getUUID());
            ItemStack stack = sender.getMainHandItem();
            Item item = stack.getItem();
            String output = stack.getDisplayName().getString() + "[x" + qty + "]";
            float cost = Main.econData.getSellPrice(item.getDescriptionId());
            if(cost <= 0f) {
                Main.textUtils.err(sender, Errs.cant_sell(stack.getDisplayName().getString()));
                return;
            } else {
                Main.getLogger().info("Cost for " + stack.getDisplayName() + " is " + Main.textUtils.parseCurrency(cost, false));
            }
            float payout = 0f;
            if(qty <= -1) qty = stack.getCount();
            if(qty <= stack.getCount()) {
                stack.setCount(stack.getCount() - qty);
                payout += cost * (float)qty;
            } else {
                qty -= stack.getCount();
                payout += cost * (float)stack.getCount();
                sender.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                for(ItemStack invStack : sender.getInventory().items) {
                    if(invStack.getItem().equals(item)) {
                        if(invStack.getCount() <= qty) {
                            invStack.setCount(invStack.getCount() - qty);
                            payout += cost * (float)qty;
                            break;
                        } else {
                            qty -= invStack.getCount();
                            payout += cost * (float)invStack.getCount();
                            invStack.setCount(0);
                        }
                    }
                }
            }
            senderData.pay(payout);
            Main.textUtils.msg(sender, Msgs.items_sold.get(output, Main.textUtils.parseCurrency(payout, true)));
        }
    }

}

