package dev.elrol.serverutilities.commands.rank;

import dev.elrol.serverutilities.Main;
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

public class RankCreate {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (Commands.literal("create")
        		.executes(RankCreate::execute))
        		.then(Commands.argument("name", StringArgumentType.string())
        				.executes(RankCreate::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String name = StringArgumentType.getString(c, "name");
        if (name.isEmpty()) {
            Main.textUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (Ranks.rankMap.containsKey(name.toLowerCase())) {
            Main.textUtils.err(c, Errs.rank_exists(name.toLowerCase()));
            return 0;
        }
        Rank rank = new Rank(name.toLowerCase());
        Ranks.rankMap.put(name.toLowerCase(), rank);
        Ranks.save(rank);
        Main.textUtils.msg(c, Msgs.rank_made.get(name.toLowerCase()));
        return 1;
    }
}

