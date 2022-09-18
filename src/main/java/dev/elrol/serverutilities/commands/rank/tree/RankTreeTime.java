package dev.elrol.serverutilities.commands.rank.tree;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.data.Rank;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankTreeTime {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("time")
                .then(Commands.argument("rank", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestRanks)
                        .then(Commands.argument("time", IntegerArgumentType.integer(0))
                                .suggests(ModSuggestions::suggestRanks)
                                .executes(RankTreeTime::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        int time = IntegerArgumentType.getInteger(c, "time");
        if (!Ranks.rankMap.containsKey(rankName)) {
            Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(rankName);
        rank.setRank_up(time);
        Main.textUtils.msg(c, Msgs.rankTimeSet.get(rankName, String.valueOf(time)));
        return 1;
    }
}

