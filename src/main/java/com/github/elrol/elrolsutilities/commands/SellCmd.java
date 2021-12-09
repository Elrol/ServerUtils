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
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class SellCmd extends _CmdBase {
    public SellCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a).executes(this::execute)));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity sender;
        try {
            sender = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(sender.getUUID());
        if (FeatureConfig.enable_economy.get() && data.getBal() >= cost) {
            if (!data.charge(cost)) {
                TextUtils.err(sender, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        if(sender.getMainHandItem().isEmpty()) {
            TextUtils.err(sender, Errs.empty_hand());
            return 0;
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(sender, -1), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity sender;
        int qty;

        public CommandRunnable(ServerPlayerEntity sender, int qty) {
            this.sender = sender;
            this.qty = qty;
        }

        @Override
        public void run() {
            IPlayerData senderData = Main.database.get(sender.getUUID());
            ItemStack stack = sender.getMainHandItem();
            Item item = stack.getItem();
            String output = stack.getDisplayName().getString() + "[x" + qty + "]";
            float cost = Main.econData.getSellPrice(item.getRegistryName());
            if(cost <= 0f) {
                TextUtils.err(sender, Errs.cant_sell(stack.getDisplayName().getString()));
                return;
            } else {
                Main.getLogger().info("Cost for " + stack.getDisplayName() + " is " + TextUtils.parseCurrency(cost, false));
            }
            float payout = 0f;
            if(qty <= -1) qty = stack.getCount();
            if(qty <= stack.getCount()) {
                stack.setCount(stack.getCount() - qty);
                payout += cost * (float)qty;
            } else {
                qty -= stack.getCount();
                payout += cost * (float)stack.getCount();
                sender.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                for(ItemStack invStack : sender.inventory.items) {
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
            TextUtils.msg(sender, Msgs.items_sold(output, TextUtils.parseCurrency(payout, true)));
        }
    }

}

