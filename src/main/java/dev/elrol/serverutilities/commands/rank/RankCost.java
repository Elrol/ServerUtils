package dev.elrol.serverutilities.commands.rank;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.data.Rank;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankCost {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("cost")
        		.then(Commands.argument("rank", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestRanks)
                        .executes(RankCost::check)
        				.then(Commands.argument("cost", FloatArgumentType.floatArg(0.0f))
        						.executes(RankCost::execute)));
    }

    private static int check(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (rankName.isEmpty()) {
            Main.textUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(rankName)) {
            Rank rank = Ranks.rankMap.get(rankName);
            Main.textUtils.msg(c, Msgs.rank_cost.get(rank.getName(), Main.textUtils.parseCurrency(rank.getRankUpCost(),true)));
            return 1;
        }
        Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
        return 0;
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (rankName.isEmpty()) {
            Main.textUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(rankName)) {
            Rank rank = Ranks.rankMap.get(rankName);
            float cost = FloatArgumentType.getFloat(c, "cost");
            rank.setRankUpCost(cost);
            Main.textUtils.msg(c, Msgs.rank_prefix.get(rank.getName(), Main.textUtils.parseCurrency(cost, false)));
            return 1;
        }
        Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
        return 0;
    }
}

