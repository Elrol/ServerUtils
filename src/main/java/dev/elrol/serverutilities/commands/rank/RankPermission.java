package dev.elrol.serverutilities.commands.rank;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RankPermission {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (Commands.literal("permission")
        		.then(RankPermissionAdd.register()))
        		.then(RankPermissionRemove.register());
    }
}

