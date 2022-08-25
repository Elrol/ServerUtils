package com.github.elrol.elrolsutilities.commands.rank.parent;

import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.data.Rank;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RankParentRemove {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return (Commands.literal("remove")
                .executes(RankParentRemove::execute))
                .then(Commands.argument("rank", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestRanks)
                        .executes(RankParentRemove::execute)
                        .then(Commands.argument("parent", StringArgumentType.string())
                            .suggests(ModSuggestions::suggestRanks)
                            .executes(RankParentRemove::execute)));
    }

    private static int execute(CommandContext<CommandSource> c) {
        String name = StringArgumentType.getString(c, "rank");
        String parent = StringArgumentType.getString(c, "parent");
        if (name.isEmpty()) {
            TextUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (!Ranks.rankMap.containsKey(name)) {
            TextUtils.err(c, Errs.rank_doesnt_exist(name));
            return 0;
        }
        if(!Ranks.rankMap.containsKey(parent)){
            TextUtils.err(c, Errs.rank_doesnt_exist(parent));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(name);
        rank.removeParent(parent);
        TextUtils.msg(c, Msgs.parentRankRemove.get(name, parent));
        return 1;
    }
}

