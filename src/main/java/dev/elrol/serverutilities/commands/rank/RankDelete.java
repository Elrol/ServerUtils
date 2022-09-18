package dev.elrol.serverutilities.commands.rank;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankDelete {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (Commands.literal("delete")
        		.executes(RankDelete::execute))
        		.then(Commands.argument("name", StringArgumentType.string())
        				.executes(RankDelete::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String name = StringArgumentType.getString(c, "name");
        if (name.isEmpty()) {
            Main.textUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (!Ranks.rankMap.containsKey(name)) {
            Main.textUtils.err(c, Errs.rank_doesnt_exist(name));
            return 0;
        }
        Ranks.remove(Ranks.rankMap.get(name));
        Main.textUtils.msg(c, Msgs.rank_removed.get(name));
        Main.serverData.updateAllPlayersWithRank(name);
        return 1;
    }
}

