package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.data.Rank;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class RankupCmd
extends _CmdBase {
    public RankupCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .executes(this::help)
                        .then(Commands.argument("name", StringArgumentType.string())
                                .suggests(ModSuggestions::suggestRankups)
                                .executes(this::execute)));
        }
    }

    protected int help(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }

        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }

        Main.textUtils.msg(player.createCommandSourceStack(), data.getDomRank().getNextRanks().toString());
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        String rank = StringArgumentType.getString(c, "name").toLowerCase();
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        if (!Ranks.rankMap.containsKey(rank)) {
            Main.textUtils.err(player, Errs.rank_doesnt_exist(rank));
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if(!data.getDomRank().getNextRanks().contains(rank)){
            Main.textUtils.err(player, Errs.rank_not_allowed(rank));
            return 0;
        }
        if(data.canRankUp())
            CommandDelay.init(this, player, new CommandRunnable(player, rank), false);
        else
            Main.textUtils.err(player, Errs.early_rankup(rank));
        return 1;
    }

    private static class CommandRunnable
            implements Runnable {
        ServerPlayer player;
        String rank;

        public CommandRunnable(ServerPlayer player, String rank) {
            this.player = player;
            this.rank = rank;
        }

        @Override
        public void run() {
            IPlayerData data = Main.database.get(player.getUUID());
            data.removeRank(data.getDomRank().getName());
            Rank r = Ranks.get(rank);
            float cost = r.getRankUpCost();
            if(cost > 0 && !data.charge(cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return;
            }
            data.addRank(r);
            data.allowRankUp(false);
            Main.textUtils.msg(this.player, Msgs.player_rank_added.get(data.getDisplayName(), rank));
            Main.database.save(player.getUUID());
            data.update();
        }
    }

}


