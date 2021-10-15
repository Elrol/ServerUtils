package com.github.elrol.elrolsutilities.commands.rank;

import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.data.Rank;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class RankInfo {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("info")
        				.then(Commands.argument("rank", StringArgumentType.string())
        						.suggests(ModSuggestions::suggestRanks)
        						.executes(RankInfo::execute));
    }

    private static int execute(CommandContext<CommandSource> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (!Ranks.rankMap.containsKey(rankName)) {
            TextUtils.err(c, Errs.rank_doesnt_exist(rankName));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(rankName);
        String[] help = new String[8 + rank.perms.size()];
        help[0] = "Name: " + rank.getName();
        help[1] = "Prefix: " + rank.getPrefix();
        help[2] = "Suffix: " + rank.getSuffix();
        help[3] = "Priority: " + rank.getWeight();
        help[4] = "Next Rank(s): " + TextUtils.listToString(rank.getNextRanks());
        help[5] = "Rank Up: " + (rank.getRankUp() > 0 ? Methods.tickToMin(rank.getRankUp()) + " Minutes" : "");
        help[6] = "Parents: " + TextUtils.listToString(rank.parents);
        help[7] = "Permissions: ";
         if(!rank.perms.isEmpty()) {
             for (int i = 0; i < rank.perms.size(); i++) {
                help[8 + i] = "- " + rank.perms.get(i);
             }
         }
         StringTextComponent helpMsg = TextUtils.commandHelp(help);
         TextUtils.msgNoTag(c.getSource(), helpMsg);
         return 1;
    }
}

