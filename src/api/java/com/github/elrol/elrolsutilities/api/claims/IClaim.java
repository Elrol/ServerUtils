package com.github.elrol.elrolsutilities.api.claims;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public interface IClaim {

    void claimChunk(ChunkPos pos);
    boolean isClaimed(BlockPos pos);
    void addClaimSetting(IClaimSettingEntry setting);
    void addChunkSetting(BlockPos pos, IClaimSettingEntry setting);

}
