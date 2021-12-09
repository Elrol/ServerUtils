package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.Price;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PriceCmd
extends _CmdBase {
    public PriceCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a).executes(this::help))
                        .executes(this::help)
                            .then(Commands.argument("buy", FloatArgumentType.floatArg(0f))
                                .then(Commands.argument("sell", FloatArgumentType.floatArg(0f))
                                    .executes(this::execute))));
        }
    }

    protected int help(CommandContext<CommandSource> c) {
        String[] help = new String[]{
                "Usage: /price [buy] [sell]",
                "[buy]: The price players will purchase the item in your main hand for.",
                "[sell]: The price players will get for selling the item in your main hand."
        };
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player = null;

        float buy = FloatArgumentType.getFloat(c, "buy");
        float sell = FloatArgumentType.getFloat(c, "sell");

        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                TextUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(player, buy, sell), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity player;
        float buy;
        float sell;
        public CommandRunnable(ServerPlayerEntity player, float buy, float sell) {
            this.player = player;
            this.buy = buy;
            this.sell = sell;
        }

        @Override
        public void run() {
            ItemStack mainHand = player.getMainHandItem();
            if(mainHand.isEmpty()) {
                TextUtils.err(player, Errs.empty_hand());
            } else {
                ResourceLocation item = mainHand.getItem().getRegistryName();
                if(item != null) {
                    Price price = Main.econData.setPrice(item, buy, sell);
                    TextUtils.msg(player, Msgs.price_change(mainHand.getDisplayName().getString(), TextUtils.parseCurrency(price.buy, true), TextUtils.parseCurrency(price.sell, true)));
                }
            }
        }
    }

}

