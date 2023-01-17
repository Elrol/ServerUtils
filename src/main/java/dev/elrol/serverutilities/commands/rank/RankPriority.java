package dev.elrol.serverutilities.commands.rank;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.data.Rank;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class RankPriority {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("priority")
        		.then(Commands.argument("rank", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestRanks)
        				.then(Commands.argument("priority", IntegerArgumentType.integer(0))
        						.executes(RankPriority::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (rankName.isEmpty()) {
            Main.textUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(rankName)) {
            int weight;
            Rank rank = Ranks.rankMap.get(rankName);

            ServerPlayer sender = null;
            try {
                sender = c.getSource().getPlayerOrException();
            } catch (CommandSyntaxException ignored) {}

            if(sender != null && !Methods.canModifyRank(sender, rank)) {
                Main.textUtils.err(c, Errs.cant_change_higher_rank.get());
                return 0;
            }

            weight = IntegerArgumentType.getInteger(c, "priority");
            rank.setWeight(weight);
            Main.textUtils.msg(c, Msgs.rank_weight.get(rank.getName(), "" + weight));
            return 1;
        }
        Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
        return 0;
    }
}

