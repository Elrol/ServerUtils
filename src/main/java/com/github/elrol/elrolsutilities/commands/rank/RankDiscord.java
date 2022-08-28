package com.github.elrol.elrolsutilities.commands.rank;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.data.Rank;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RankDiscord {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("discord")
                .then(Commands.argument("rank", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestRanks)
                        .then(Commands.argument("server id", LongArgumentType.longArg(0))
                                .then(Commands.argument("role id", LongArgumentType.longArg(0))
                                        .executes(RankDiscord::execute))));
    }

    private static int execute(CommandContext<CommandSource> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (!Ranks.rankMap.containsKey(rankName)) {
            TextUtils.err(c, Errs.rank_doesnt_exist(rankName));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(rankName);
        long serverId = LongArgumentType.getLong(c, "server id");
        long roleId = LongArgumentType.getLong(c, "role id");

        Role role = Main.bot.getRole(serverId,roleId);
        if(role == null) {
            TextUtils.err(c.getSource(), Errs.role_missing.get());
            return 0;
        }

        rank.addDiscordID(serverId, roleId);
        Ranks.save(rank);
        TextUtils.msg(c.getSource(), Msgs.roleAdded.get(role.getName(), role.getGuild().getName(), rank.getName()));
        return 1;
    }
}
