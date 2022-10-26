package dev.elrol.serverutilities.api.perms;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public interface IPermissionHandler {
    void addPermission(String perm);
    boolean hasPermission(UUID uuid, String perm);
    boolean hasPermission(ServerPlayer player, String perm);
    boolean hasPermission(CommandSourceStack source, IPermission perm);
    boolean hasPermission(CommandSourceStack source, String perm);
    boolean hasPermission(CommandSourceStack source, String node, String perm);
    boolean hasChunkPermission(ServerPlayer player, BlockPos pos);
}
