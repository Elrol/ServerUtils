package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.data.Price;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PriceCmd
extends _CmdBase {
    public PriceCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a).executes(this::help))
                        .executes(this::help)
                            .then(Commands.argument("buy", FloatArgumentType.floatArg(0f))
                                .then(Commands.argument("sell", FloatArgumentType.floatArg(0f))
                                    .executes(this::execute))));
        }
    }

    protected int help(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{
                "Usage: /price [buy] [sell]",
                "[buy]: The price players will purchase the item in your main hand for.",
                "[sell]: The price players will get for selling the item in your main hand."
        };
        Main.textUtils.msgNoTag(c.getSource(), Main.textUtils.commandHelp(help));
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;

        float buy = FloatArgumentType.getFloat(c, "buy");
        float sell = FloatArgumentType.getFloat(c, "sell");

        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(player, buy, sell), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer player;
        float buy;
        float sell;
        public CommandRunnable(ServerPlayer player, float buy, float sell) {
            this.player = player;
            this.buy = buy;
            this.sell = sell;
        }

        @Override
        public void run() {
            ItemStack mainHand = player.getMainHandItem();
            if(mainHand.isEmpty()) {
                Main.textUtils.err(player, Errs.empty_hand());
            } else {
                String item = mainHand.getItem().getDescriptionId();
                if(!item.isEmpty()) {
                    Price price = Main.econData.setPrice(item, buy, sell);
                    Main.textUtils.msg(player, Msgs.price_change.get(mainHand.getDisplayName().getString(), Main.textUtils.parseCurrency(price.buy, true), Main.textUtils.parseCurrency(price.sell, true)));
                }
            }
        }
    }

}

