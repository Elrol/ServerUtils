package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.data.Permission;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ChestShopCmd extends _CmdBase {
    public ChestShopCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : this.aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute)));
        }
        new Permission(CommandConfig.chestshop_adminperm.get());
        new Permission(CommandConfig.chestshop_max_perm.get());
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{
                "To create a shop",
                "1: Place a sign with either [sell] or [buy] on the first line (or [adminbuy] / [adminsell])",
                "2: Lines 2 and 3 are free to use for the description/info for players",
                "3: Set the price for each transaction on the fourth line.",
                "4: Place a chest somewhere with the item(s) that you want to buy/sell, per transaction (It can be multiple stacks if you want)",
                "5: Right click the sign with redstone, then right click on the chest."
        };
        Main.textUtils.msgNoTag(c.getSource(), Main.textUtils.commandHelp(help));
        return 1;
    }
}

