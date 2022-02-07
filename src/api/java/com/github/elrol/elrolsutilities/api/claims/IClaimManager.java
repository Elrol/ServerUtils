package com.github.elrol.elrolsutilities.api.claims;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public interface IClaimManager {

    IClaim getOrCreate(UUID uuid);
    void claim(UUID uuid, BlockPos pos);
    void claim(ServerPlayer player);

}
