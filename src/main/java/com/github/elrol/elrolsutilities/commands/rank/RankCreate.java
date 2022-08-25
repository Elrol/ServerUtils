package com.github.elrol.elrolsutilities.commands.rank;

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

public class RankCreate {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return (Commands.literal("create")
        		.executes(RankCreate::execute))
        		.then(Commands.argument("name", StringArgumentType.string())
        				.executes(RankCreate::execute));
    }

    private static int execute(CommandContext<CommandSource> c) {
        String name = StringArgumentType.getString(c, "name");
        if (name.isEmpty()) {
            TextUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(name.toLowerCase())) {
            TextUtils.err(c, Errs.rank_exists(name.toLowerCase()));
            return 0;
        }
        Rank rank = new Rank(name.toLowerCase());
        Ranks.rankMap.put(name.toLowerCase(), rank);
        Ranks.save(rank);
        TextUtils.msg(c, Msgs.rank_made.get(name.toLowerCase()));
        return 1;
    }
}

