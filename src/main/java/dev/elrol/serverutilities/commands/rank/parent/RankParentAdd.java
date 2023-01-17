package dev.elrol.serverutilities.commands.rank.parent;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IRank;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class RankParentAdd {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (Commands.literal("add")
                .executes(RankParentAdd::execute))
                .then(Commands.argument("rank", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestRanks)
                        .executes(RankParentAdd::execute)
                        .then(Commands.argument("parent", StringArgumentType.string())
                                .suggests(ModSuggestions::suggestRanks)
                                .executes(RankParentAdd::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String name = StringArgumentType.getString(c, "rank");
        String parent = StringArgumentType.getString(c, "parent");
        if (name.isEmpty()) {
            Main.textUtils.err(c, Errs.no_rank_name());
            return 0;
        }
        if (!Ranks.rankMap.containsKey(name)) {
            Main.textUtils.err(c, Errs.rank_doesnt_exist(name));
            return 0;
        }
        if(!Ranks.rankMap.containsKey(parent)){
            Main.textUtils.err(c, Errs.rank_doesnt_exist(parent));
            return 0;
        }

        IRank rank = Ranks.rankMap.get(name);
        IRank parentRank = Ranks.rankMap.get(parent);
        ServerPlayer sender = null;
        try {
            sender = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException ignored) {}

        if(sender != null && !Methods.canModifyRank(sender, rank) && !Methods.canModifyRank(sender, parentRank)) {
            Main.textUtils.err(c, Errs.cant_change_higher_rank.get());
            return 0;
        }

        rank.addParent(parent);
        Main.textUtils.msg(c, Msgs.parentRankAdd.get(name, parent));
        return 1;
    }
}

