package com.github.elrol.elrolsutilities.commands.rank;

import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.data.Rank;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RankCost {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("cost")
        		.then(Commands.argument("rank", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestRanks)
                        .executes(RankCost::check)
        				.then(Commands.argument("cost", FloatArgumentType.floatArg(0.0f))
        						.executes(RankCost::execute)));
    }

    private static int check(CommandContext<CommandSource> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (rankName.isEmpty()) {
            TextUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(rankName)) {
            Rank rank = Ranks.rankMap.get(rankName);
            TextUtils.msg(c, Msgs.rank_cost.get(rank.getName(), TextUtils.parseCurrency(rank.getRankUpCost(),true)));
            return 1;
        }
        TextUtils.err(c, Errs.rank_doesnt_exist(rankName));
        return 0;
    }

    private static int execute(CommandContext<CommandSource> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (rankName.isEmpty()) {
            TextUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(rankName)) {
            Rank rank = Ranks.rankMap.get(rankName);
            float cost = FloatArgumentType.getFloat(c, "cost");
            rank.setRankUpCost(cost);
            TextUtils.msg(c, Msgs.rank_prefix.get(rank.getName(), TextUtils.parseCurrency(cost, false)));
            return 1;
        }
        TextUtils.err(c, Errs.rank_doesnt_exist(rankName));
        return 0;
    }
}

