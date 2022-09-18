package dev.elrol.serverutilities.commands.rank;

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

public class RankCmdsRemove {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("remove")
        		.then(Commands.argument("rank", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestRanks)
        				.then(Commands.argument("index", IntegerArgumentType.integer(0))
        						.executes(RankCmdsRemove::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (rankName.isEmpty()) {
            Main.textUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(rankName)) {
            Rank rank = Ranks.rankMap.get(rankName);
            int index = IntegerArgumentType.getInteger(c, "index");
            String cmd = rank.getCmd(index);
            if(cmd.isEmpty()) {
                Main.textUtils.err(c, Errs.rank_cmd_null(cmd, String.valueOf(index)));
                return 0;
            }
            Main.textUtils.msg(c, Msgs.rank_cmd_removed.get(cmd, rank.getName()));
            return 1;
        }
        Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
        return 0;
    }
}

