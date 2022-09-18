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

public class RankCmdsClear {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("clear")
        		.then(Commands.argument("rank", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestRanks)
                        .executes(RankCmdsClear::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (rankName.isEmpty()) {
            Main.textUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(rankName)) {
            Rank rank = Ranks.rankMap.get(rankName);
            rank.clearCmds();
            Main.textUtils.msg(c, Msgs.rank_cmd_cleared.get(rank.getName()));
            return 1;
        }
        Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
        return 0;
    }
}

