package dev.elrol.serverutilities.commands.rank;

import dev.elrol.serverutilities.commands.rank.tree.RankTreeAdd;
import dev.elrol.serverutilities.commands.rank.tree.RankTreeRemove;
import dev.elrol.serverutilities.commands.rank.tree.RankTreeTime;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankTree {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("tree")
                .then(RankTreeAdd.register())
                .then(RankTreeRemove.register())
                .then(RankTreeTime.register());
    }
}
