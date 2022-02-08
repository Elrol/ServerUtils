package com.github.elrol.elrolsutilities.data;

import com.github.elrol.elrolsutilities.api.claims.IClaim;
import com.github.elrol.elrolsutilities.api.claims.IClaimSettingEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.UUID;
import java.util.function.Function;

public class Claim implements IClaim {
    private UUID uuid;
    private Function<BlockPos, Boolean> delegate;

    public Claim(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void claimChunk(ChunkPos pos) {

    }

    @Override
    public boolean isClaimed(BlockPos pos) {
        return false;
    }

    @Override
    public void addClaimSetting(IClaimSettingEntry setting) {

    }

    @Override
    public void addChunkSetting(BlockPos pos, IClaimSettingEntry setting) {

    }
}
