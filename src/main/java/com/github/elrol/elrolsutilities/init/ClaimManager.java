package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.api.claims.IClaim;
import com.github.elrol.elrolsutilities.api.claims.IClaimManager;
import com.github.elrol.elrolsutilities.api.claims.IClaimSetting;
import com.github.elrol.elrolsutilities.data.claimSettings.Claim;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

import java.util.*;

public class ClaimManager implements IClaimManager {

    private List<IClaimSetting> settings = new ArrayList<>();
    private Map<UUID, IClaim> claims = new HashMap<>();

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
    public void claim(ServerPlayer player) {
        claim(player.getUUID(), player.blockPosition());
    }
}
