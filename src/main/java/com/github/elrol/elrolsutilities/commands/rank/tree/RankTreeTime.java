package com.github.elrol.elrolsutilities.commands.rank.tree;

import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.data.Rank;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RankTreeTime {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("time")
                .then(Commands.argument("rank", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestRanks)
                        .then(Commands.argument("time", IntegerArgumentType.integer(0))
                                .suggests(ModSuggestions::suggestRanks)
                                .executes(RankTreeTime::execute)));
    }

    private static int execute(CommandContext<CommandSource> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        int time = IntegerArgumentType.getInteger(c, "time");
        if (!Ranks.rankMap.containsKey(rankName)) {
            TextUtils.err(c, Errs.rank_doesnt_exist(rankName));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(rankName);
        rank.setRank_up(time);
        TextUtils.msg(c, Msgs.rankTimeSet.get(rankName, String.valueOf(time)));
        return 1;
    }
}

