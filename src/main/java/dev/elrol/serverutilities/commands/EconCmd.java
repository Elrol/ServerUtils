package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands.econ.EconAdd;
import dev.elrol.serverutilities.commands.econ.EconBal;
import dev.elrol.serverutilities.commands.econ.EconRemove;
import dev.elrol.serverutilities.commands.econ.EconSet;
import dev.elrol.serverutilities.libs.text.TextUtils;
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

    protected int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{
                "Add:Gives the specified player the amount entered",
                "Remove:Takes the amount entered from the player",
                "Bal: Shows the balance of the target player"
        };
        Main.textUtils.msgNoTag(c.getSource(), Main.textUtils.commandHelp(help));
        return 1;
    }
}
