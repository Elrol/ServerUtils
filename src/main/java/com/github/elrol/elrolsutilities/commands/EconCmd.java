package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.commands.econ.EconAdd;
import com.github.elrol.elrolsutilities.commands.econ.EconBal;
import com.github.elrol.elrolsutilities.commands.econ.EconRemove;
import com.github.elrol.elrolsutilities.commands.econ.EconSet;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class EconCmd extends _CmdBase {

    public EconCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : this.aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .executes(this::execute)
                        .then(EconAdd.register())
                        .then(EconRemove.register())
                        .then(EconBal.register())
                        .then(EconSet.register())
                );
        }
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{
                "Add:Gives the specified player the amount entered",
                "Remove:Takes the amount entered from the player",
                "Bal: Shows the balance of the target player"
        };
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }
}
