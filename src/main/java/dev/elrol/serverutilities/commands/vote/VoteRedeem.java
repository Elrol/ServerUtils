package dev.elrol.serverutilities.commands.vote;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class VoteRedeem {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("redeem").executes(VoteRedeem::execute);
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        try {
            ServerPlayer player = c.getSource().getPlayerOrException();
            IPlayerData data = Main.database.get(player.getUUID());
            if(data.getVoteRewardCount() > 0) {
                for(int i = 0; i < data.getVoteRewardCount(); i++) {
                    Main.vote.grantVoteReward(player);
                }
                data.clearVoteReward();
            } else {
                Main.textUtils.err(player, Errs.no_vote_rewards_left.get());
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        return 1;
    }
}

