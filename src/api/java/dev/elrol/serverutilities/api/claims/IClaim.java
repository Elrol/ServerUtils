package dev.elrol.serverutilities.api.claims;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

public interface IClaim {

    void claimChunk(ChunkPos pos);
    boolean isClaimed(BlockPos pos);
    void addClaimSetting(dev.elrol.serverutilities.api.claims.IClaimSettingEntry setting);
    void addChunkSetting(BlockPos pos, dev.elrol.serverutilities.api.claims.IClaimSettingEntry setting);

}
