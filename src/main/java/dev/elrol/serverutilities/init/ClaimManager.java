package dev.elrol.serverutilities.init;

import dev.elrol.serverutilities.api.claims.IClaim;
import dev.elrol.serverutilities.api.claims.IClaimManager;
import dev.elrol.serverutilities.api.claims.IClaimSetting;
import dev.elrol.serverutilities.data.claimSettings.Claim;
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
