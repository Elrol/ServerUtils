package com.github.elrol.elrolsutilities.commands.rank;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RankDelete {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return (Commands.literal("delete")
        		.executes(RankDelete::execute))
        		.then(Commands.argument("name", StringArgumentType.string())
        				.executes(RankDelete::execute));
    }

    private static int execute(CommandContext<CommandSource> c) {
        String name = StringArgumentType.getString(c, "name");
        if (name.isEmpty()) {
            TextUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (!Ranks.rankMap.containsKey(name)) {
            TextUtils.err(c, Errs.rank_doesnt_exist(name));
            return 0;
        }
        Ranks.remove(Ranks.rankMap.get(name));
        TextUtils.msg(c, Msgs.rank_removed(name));
        Main.serverData.updateAllPlayersWithRank(name);
        return 1;
    }
}

