package com.github.elrol.elrolsutilities.commands.rank;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.data.Rank;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.UUID;

public class RankRemove {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("remove")
        		.then(Commands.argument("player", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestPlayers)
        				.then(Commands.argument("rank", StringArgumentType.string())
        						.suggests(ModSuggestions::suggestRanks)
        						.executes(RankRemove::execute)));
    }

    private static int execute(CommandContext<CommandSource> c) {
        Rank rank = Ranks.rankMap.get(StringArgumentType.getString(c, "rank"));
        String name = StringArgumentType.getString(c, "player");
        UUID uuid = Methods.getUUIDFromName(name);
        if (uuid == null) {
            TextUtils.err(c, Errs.player_not_found(name));
            return 0;
        }
        IPlayerData data = Main.database.get(uuid);
        if (!data.getRanks().contains(rank.getName())) {
            TextUtils.err(c, Errs.player_missing_rank(data.getDisplayName(), rank.getName()));
            return 0;
        }
        TextUtils.msg(c, Msgs.player_rank_removed.get(data.getDisplayName(), rank.getName()));
        data.removeRank(rank.getName());
        Main.database.save(uuid);
        data.update();
        return 1;
    }
}

