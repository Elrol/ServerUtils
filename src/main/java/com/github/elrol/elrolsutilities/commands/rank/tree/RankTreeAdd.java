package com.github.elrol.elrolsutilities.commands.rank.tree;

import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.data.Rank;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankTreeAdd {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("add")
                .then(Commands.argument("rank", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestRanks)
        				.then(Commands.argument("next_rank", StringArgumentType.string())
        						.suggests(ModSuggestions::suggestRanks)
        						.executes(RankTreeAdd::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        String nextName = StringArgumentType.getString(c, "next_rank");
        if (!Ranks.rankMap.containsKey(rankName)) {
            TextUtils.err(c, Errs.rank_doesnt_exist(rankName));
            return 0;
        }
        if (!Ranks.rankMap.containsKey(nextName)) {
            TextUtils.err(c, Errs.rank_doesnt_exist(nextName));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(rankName);
        rank.addNextRank(nextName);
        TextUtils.msg(c, Msgs.rankAddedToTree(nextName, rankName));
        return 1;
    }
}

