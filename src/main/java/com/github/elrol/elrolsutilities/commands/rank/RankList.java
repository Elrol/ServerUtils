package com.github.elrol.elrolsutilities.commands.rank;

import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RankList {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("list").executes(RankList::execute);
    }

    private static int execute(CommandContext<CommandSource> c) {
        String[] help = new String[1 + Ranks.rankMap.size()];
        help[0] = "Ranks: ";
        int i = 0;
        for(String rank : Ranks.rankMap.keySet()) {
            help[1 + i] = "- " + rank;
            i++;
        }
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }
}

