package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.Rank;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class RankupCmd
extends _CmdBase {
    public RankupCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .executes(this::help)
                        .then(Commands.argument("name", StringArgumentType.string())
                                .suggests(ModSuggestions::suggestRankups)
                                .executes(this::execute)));
        }
    }

    protected int help(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
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
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }

        TextUtils.msg(player.createCommandSourceStack(), data.getDomRank().getNextRanks().toString());
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        String rank = StringArgumentType.getString(c, "name").toLowerCase();
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
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
            TextUtils.err(player, Errs.rank_doesnt_exist(rank));
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if(!data.getDomRank().getNextRanks().contains(rank)){
            TextUtils.err(player, Errs.rank_not_allowed(rank));
            return 0;
        }
        if(data.canRankUp())
            CommandDelay.init(this, player, new CommandRunnable(player, rank), false);
        else
            TextUtils.err(player, Errs.early_rankup(rank));
        return 1;
    }

    private static class CommandRunnable
            implements Runnable {
        ServerPlayerEntity player;
        String rank;

        public CommandRunnable(ServerPlayerEntity player, String rank) {
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
                TextUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return;
            }
            data.addRank(r);
            data.allowRankUp(false);
            TextUtils.msg(this.player, Msgs.player_rank_added.get(data.getDisplayName(), rank));
            Main.database.save(player.getUUID());
            data.update();
        }
    }

}


