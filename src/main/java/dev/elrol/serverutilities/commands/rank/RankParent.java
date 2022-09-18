package dev.elrol.serverutilities.commands.rank;

import dev.elrol.serverutilities.commands.rank.parent.RankParentAdd;
import dev.elrol.serverutilities.commands.rank.parent.RankParentRemove;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankParent {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (Commands.literal("parent")
                .then(RankParentAdd.register()))
                .then(RankParentRemove.register());
    }
}

