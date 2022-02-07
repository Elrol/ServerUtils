package com.github.elrol.elrolsutilities.commands.rank;

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

public class RankPrefix {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("prefix")
        		.then(Commands.argument("rank", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestRanks)
        				.then(Commands.argument("prefix", StringArgumentType.string())
        						.executes(RankPrefix::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (rankName.isEmpty()) {
            TextUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(rankName)) {
            String prefix;
            Rank rank = Ranks.rankMap.get(rankName);
            prefix = StringArgumentType.getString(c, "prefix");
            rank.setPrefix(prefix);
            TextUtils.msg(c, Msgs.rank_prefix(rank.getName(), TextUtils.formatString(prefix)));
            return 1;
        }
        TextUtils.err(c, Errs.rank_doesnt_exist(rankName));
        return 0;
    }
}

