package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.commands.rank.*;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class RankCmd
extends _CmdBase {
    public RankCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .executes(this::execute)
                        .then(RankCreate.register())
                        .then(RankDelete.register())
                        .then(RankAdd.register())
                        .then(RankRemove.register())
                        .then(RankPermission.register())
                        .then(RankPriority.register())
                        .then(RankPrefix.register())
                        .then(RankSuffix.register())
                        .then(RankTree.register())
                        .then(RankInfo.register())
                        .then(RankList.register())
                        .then(RankCmds.register())
                        .then(RankParent.register()));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity player;

        public CommandRunnable(ServerPlayerEntity player) {
            this.player = player;
        }

        @Override
        public void run() {
            IPlayerData data = Main.database.get(this.player.getUUID());
            TextUtils.msg(this.player, Msgs.rank_check(data.getStringRanks()));
        }
    }

}

