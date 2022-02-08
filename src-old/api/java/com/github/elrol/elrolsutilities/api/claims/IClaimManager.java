package com.github.elrol.elrolsutilities.api.claims;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public interface IClaimManager {

    IClaim getOrCreate(UUID uuid);
    void claim(UUID uuid, BlockPos pos);
    void claim(ServerPlayerEntity player);

}
