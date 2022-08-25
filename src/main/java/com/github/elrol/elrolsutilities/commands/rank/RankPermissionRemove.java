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
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RankPermissionRemove {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("remove")
        		.then(Commands.argument("rank", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestRanks)
        				.then(Commands.argument("perm", StringArgumentType.greedyString())
        						.suggests(ModSuggestions::suggestPerms)
        						.executes(RankPermissionRemove::execute)));
    }

    public static int execute(CommandContext<CommandSource> c) {
        Rank rank = Ranks.rankMap.get(StringArgumentType.getString(c, "rank"));
        String perm = StringArgumentType.getString(c, "perm");
        if (perm.contains(" ")) {
            String s = "";
            for (String split : perm.split(" ")) {
                s = s.isEmpty() ? split : s + "_" + split;
            }
            perm = s;
        }
        if (rank.removePerm(perm)) {
            TextUtils.msg(c, Msgs.rank_perm_removed.get(perm, rank.getName()));
            //Main.serverData.updateAllPlayersWithRank(rank.getName());
            return 1;
        }
        TextUtils.err(c, Errs.rank_perm_doesnt_exists(rank.getName(), perm));
        return 0;
    }
}

