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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankParentAdd {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (Commands.literal("add")
                .executes(RankParentAdd::execute))
                .then(Commands.argument("rank", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestRanks)
                        .executes(RankParentAdd::execute)
                        .then(Commands.argument("parent", StringArgumentType.string())
                                .suggests(ModSuggestions::suggestRanks)
                                .executes(RankParentAdd::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
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
        rank.addParent(parent);
        TextUtils.msg(c, Msgs.parentRankAdd(name, parent));
        return 1;
    }
}

