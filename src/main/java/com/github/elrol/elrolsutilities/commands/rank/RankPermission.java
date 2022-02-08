package com.github.elrol.elrolsutilities.commands.rank;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RankPermission {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return (Commands.literal("permission")
        		.then(RankPermissionAdd.register()))
        		.then(RankPermissionRemove.register());
    }
}

