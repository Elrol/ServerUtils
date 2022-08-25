package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.commands.item.ItemCopy;
import com.github.elrol.elrolsutilities.commands.item.ItemLore;
import com.github.elrol.elrolsutilities.commands.item.ItemModel;
import com.github.elrol.elrolsutilities.commands.item.ItemName;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ItemCmd extends _CmdBase {

    public ItemCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(
                        Commands.literal(a)
                                .executes(this::execute)
                                .then(ItemName.register())
                                .then(ItemModel.register())
                                .then(ItemLore.register())
                                .then(ItemCopy.register()));
        }
    }

    protected int execute(CommandContext<CommandSource> c){
        String[] help = new String[]{
                "Name: Change the name of the item in your main hand.",
                "Lore: Allows for changes to the item's lore.",
                "Model: Allows you to set or clear the CustomModelData.",
                "Copy: Will copy the item and data to your clipboard"
        };
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }
}

