package dev.elrol.serverutilities.commands.rank;

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

public class RankPermissionAdd {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("add")
        		.then(Commands.argument("rank", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestRanks)
        				.then(Commands.argument("perm", StringArgumentType.greedyString())
        						.suggests(ModSuggestions::suggestPerms)
        						.executes(RankPermissionAdd::execute)));
    }

    public static int execute(CommandContext<CommandSourceStack> c) {
        Rank rank = Ranks.rankMap.get(StringArgumentType.getString(c, "rank"));
        String perm = StringArgumentType.getString(c, "perm");
        if (perm.contains(" ")) {
            String s = "";
            for (String split : perm.split(" ")) {
                s = s.isEmpty() ? split : s + "_" + split;
            }
            perm = s;
        }
        if (rank.addPerm(perm)) {
            Main.textUtils.msg(c, Msgs.rank_perm_added.get(perm, rank.getName()));
            //Main.serverData.updateAllPlayersWithRank(rank.getName());
            return 1;
        }
        Main.textUtils.err(c, Errs.rank_perm_exists(rank.getName(), perm));
        return 0;
    }
}

