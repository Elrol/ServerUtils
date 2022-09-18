package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands.item.ItemCopy;
import dev.elrol.serverutilities.commands.item.ItemLore;
import dev.elrol.serverutilities.commands.item.ItemModel;
import dev.elrol.serverutilities.commands.item.ItemName;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ItemCmd extends _CmdBase {

    public ItemCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
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

    protected int execute(CommandContext<CommandSourceStack> c){
        String[] help = new String[]{
                "Name: Change the name of the item in your main hand.",
                "Lore: Allows for changes to the item's lore."
        };
        Main.textUtils.msgNoTag(c.getSource(), Main.textUtils.commandHelp(help));
        return 1;
    }
}

