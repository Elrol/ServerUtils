package dev.elrol.serverutilities.commands.rank;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.data.Rank;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class RankAdd {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("add")
        		.then(Commands.argument("player", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestPlayers)
        				.then(Commands.argument("rank", StringArgumentType.string())
        						.suggests(ModSuggestions::suggestRanks)
        						.executes(RankAdd::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String rankName = StringArgumentType.getString(c, "rank");
        if (!Ranks.rankMap.containsKey(rankName)) {
            Main.textUtils.err(c, Errs.rank_doesnt_exist(rankName));
            return 0;
        }
        Rank rank = Ranks.rankMap.get(rankName);
        String name = StringArgumentType.getString(c, "player");
        UUID uuid = Methods.getUUIDFromName(name);
        if (uuid == null) {
            Main.textUtils.err(c, Errs.player_not_found(name));
            return 0;
        }
        IPlayerData data = Main.database.get(uuid);
        if (data.getRanks().contains(rank.getName())) {
            Main.textUtils.err(c, Errs.player_has_rank(data.getDisplayName(), rank.getName()));
            return 0;
        }

        ServerPlayer sender = null;
        try {
            sender = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException ignored) {}

        if(sender != null && !Methods.canModifyRank(sender, rank)) {
            Main.textUtils.err(c, Errs.cant_change_higher_rank.get());
            return 0;
        }

        Main.textUtils.msg(c, Msgs.player_rank_added.get(data.getDisplayName(), rank.getName()));
        data.addRank(rank);
        c.getSource().getServer().getPlayerList().getPlayer(uuid).refreshTabListName();
        Main.database.save(uuid);
        data.update();
        return 1;
    }
}

