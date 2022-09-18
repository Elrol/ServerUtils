package dev.elrol.serverutilities.data;

import dev.elrol.serverutilities.api.claims.IClaim;
import dev.elrol.serverutilities.api.claims.IClaimSettingEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

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
