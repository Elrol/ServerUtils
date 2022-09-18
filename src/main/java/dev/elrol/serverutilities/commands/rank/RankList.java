package dev.elrol.serverutilities.commands.rank;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankList {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("list").executes(RankList::execute);
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[1 + Ranks.rankMap.size()];
        help[0] = "Ranks: ";
        int i = 0;
        for(String rank : Ranks.rankMap.keySet()) {
            help[1 + i] = "- " + rank;
            i++;
        }
        Main.textUtils.msgNoTag(c.getSource(), Main.textUtils.commandHelp(help));
        return 1;
    }
}

