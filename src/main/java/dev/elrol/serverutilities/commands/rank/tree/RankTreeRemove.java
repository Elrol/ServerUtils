package dev.elrol.serverutilities.commands.rank.tree;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.data.Rank;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankTreeRemove {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("remove")
                .then(Commands.argument("rank", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestRanks)
                        .then(Commands.argument("next_rank", StringArgumentType.string())
                                .suggests(ModSuggestions::suggestRanks)
                                .executes(RankTreeRemove::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        String nextName = StringArgumentType.getString(c, "next_rank");
        if (!Ranks.rankMap.containsKey(rankName)) {
            Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
            return 0;
        }
        if (!Ranks.rankMap.containsKey(nextName)) {
            Main.textUtils.err(c, Errs.rank_doesnt_exist(nextName));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(rankName);
        rank.removeNextRank(nextName);
        Main.textUtils.msg(c, Msgs.rankRemoveFromTree.get(nextName, rankName));
        return 1;
    }
}

