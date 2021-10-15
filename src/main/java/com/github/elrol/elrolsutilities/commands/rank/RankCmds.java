package com.github.elrol.elrolsutilities.commands.rank;

import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class RankCmds {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("cmds")
                .executes(RankCmds::execute)
                .then(RankCmdsAdd.register())
                .then(RankCmdsRemove.register())
                .then(RankCmdsClear.register());
    }

    private static int execute(CommandContext<CommandSource> c) {
        String[] help = new String[]{
                "A Rank's Commands will be ran when a player gets the Rank",
                "- {player} will be replaced by the players username.",
                "Add: Adds a command to the Rank's Commands.",
                "Remove: Removes the command at the index specified.",
                "Clear: Removes all commands from the Rank."
        };
        StringTextComponent helpMsg = TextUtils.commandHelp(help);
         TextUtils.msgNoTag(c.getSource(), helpMsg);
         return 1;
    }
}

