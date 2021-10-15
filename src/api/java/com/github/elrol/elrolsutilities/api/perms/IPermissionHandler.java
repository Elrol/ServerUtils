package com.github.elrol.elrolsutilities.api.perms;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;
import java.util.UUID;

public interface IPermissionHandler {

    boolean hasPermission(UUID uuid, String perm);
    boolean hasPermission(ServerPlayerEntity player, String perm);
    boolean hasPermission(CommandSource source, IPermission perm);
    boolean hasPermission(CommandSource source, String perm);
    boolean hasPermission(CommandSource source, String node, String perm);
}
