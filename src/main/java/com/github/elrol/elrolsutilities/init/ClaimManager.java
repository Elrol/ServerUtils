package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.api.claims.IClaim;
import com.github.elrol.elrolsutilities.api.claims.IClaimManager;
import com.github.elrol.elrolsutilities.api.claims.IClaimSetting;
import com.github.elrol.elrolsutilities.data.claimSettings.Claim;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;

import java.util.*;

public class ClaimManager implements IClaimManager {

    private final List<IClaimSetting> settings = new ArrayList<>();
    private final Map<UUID, IClaim> claims = new HashMap<>();

    public void register(IClaimSetting setting) {

    }

    @Override
    public IClaim getOrCreate(UUID uuid) {
        return claims.getOrDefault(uuid, new Claim());
    }

    @Override
    public void claim(UUID uuid, BlockPos pos) {
        IClaim claim = getOrCreate(uuid);
        claim.claimChunk(new ChunkPos(pos));
        claims.put(uuid, claim);
    }

    @Override
    public void claim(ServerPlayerEntity player) {
        claim(player.getUUID(), player.blockPosition());
    }
}
