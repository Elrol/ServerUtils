package dev.elrol.serverutilities.commands.rank;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.data.Rank;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class RankInfo {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("info")
        				.then(Commands.argument("rank", StringArgumentType.string())
        						.suggests(ModSuggestions::suggestCurrentRanks)
        						.executes(RankInfo::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (!Ranks.rankMap.containsKey(rankName)) {
            Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(rankName);
        String[] help = new String[8 + rank.perms.size()];
        help[0] = "Name: " + rank.getName();
        help[1] = "Prefix: " + rank.getPrefix();
        help[2] = "Suffix: " + rank.getSuffix();
        help[3] = "Priority: " + rank.getWeight();
        help[4] = "Next Rank(s): " + Main.textUtils.listToString(rank.getNextRanks());
        help[5] = "Rank Up: " + (rank.getRankUp() > 0 ? Methods.tickToMin(rank.getRankUp()) + " Minutes" : "");
        help[6] = "Parents: " + Main.textUtils.listToString(rank.parents);
        help[7] = "Permissions: ";
         if(!rank.perms.isEmpty()) {
             for (int i = 0; i < rank.perms.size(); i++) {
                help[8 + i] = "- " + rank.perms.get(i);
             }
         }
         Component helpMsg = Main.textUtils.commandHelp(help);
         Main.textUtils.msgNoTag(c.getSource(), helpMsg);
         return 1;
    }
}

