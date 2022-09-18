package dev.elrol.serverutilities.commands.rank;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class RankCmds {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("cmds")
                .executes(RankCmds::execute)
                .then(RankCmdsAdd.register())
                .then(RankCmdsRemove.register())
                .then(RankCmdsClear.register());
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{
                "A Rank's Commands will be ran when a player gets the Rank",
                "- {player} will be replaced by the players username.",
                "Add: Adds a command to the Rank's Commands.",
                "Remove: Removes the command at the index specified.",
                "Clear: Removes all commands from the Rank."
        };
        Component helpMsg = Main.textUtils.commandHelp(help);
        Main.textUtils.msgNoTag(c.getSource(), helpMsg);
        return 1;
    }
}

