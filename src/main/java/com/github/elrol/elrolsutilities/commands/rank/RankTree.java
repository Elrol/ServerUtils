package com.github.elrol.elrolsutilities.commands.rank;

import com.github.elrol.elrolsutilities.commands.rank.tree.RankTreeAdd;
import com.github.elrol.elrolsutilities.commands.rank.tree.RankTreeRemove;
import com.github.elrol.elrolsutilities.commands.rank.tree.RankTreeTime;
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
