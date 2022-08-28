package com.github.elrol.elrolsutilities.commands.vote;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class VoteRedeem {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("redeem").executes(VoteRedeem::execute);
    }

    private static int execute(CommandContext<CommandSource> c) {
        try {
            ServerPlayerEntity player = c.getSource().getPlayerOrException();
            IPlayerData data = Main.database.get(player.getUUID());
            if(data.getVoteRewardCount() > 0) {
                for(int i = 0; i < data.getVoteRewardCount(); i++) {
                    Main.vote.grantVoteReward(player);
                }
                data.clearVoteReward();
            } else {
                TextUtils.err(player, Errs.no_vote_rewards_left.get());
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        return 1;
    }
}
