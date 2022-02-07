package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.data.Permission;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
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

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{
                "To create a shop",
                "1: Place a sign with either [sell] or [buy] on the first line (or [adminbuy] / [adminsell])",
                "2: Set the quantity on the second line, if left blank will default to 1",
                "3: Set the price for the quantity on the third line.",
                "4: Place a chest somewhere with the item that you want to buy/sell.",
                "5: Right click the sign with redstone, then right click on the chest."
        };
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }
}

