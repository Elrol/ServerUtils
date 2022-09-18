package dev.elrol.serverutilities.commands.rank;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.data.Rank;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankDiscord {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("discord")
        		.then(Commands.argument("rank", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestRanks)
                        .then(Commands.argument("server id", LongArgumentType.longArg(0))
        				    .then(Commands.argument("role id", LongArgumentType.longArg(0))
        						    .executes(RankDiscord::execute))));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (!Ranks.rankMap.containsKey(rankName)) {
            Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(rankName);
        long serverId = LongArgumentType.getLong(c, "server id");
        long roleId = LongArgumentType.getLong(c, "role id");

        Role role = Main.bot.getRole(serverId,roleId);
        if(role == null) {
            Main.textUtils.err(c.getSource(), Errs.role_missing.get());
            return 0;
        }

        rank.addDiscordID(serverId, roleId);
        Ranks.save(rank);
        Main.textUtils.msg(c.getSource(), Msgs.roleAdded.get(role.getName(), role.getGuild().getName(), rank.getName()));
        return 1;
    }
}

