package com.github.elrol.elrolsutilities.data.claimSettings;

import com.github.elrol.elrolsutilities.api.claims.IClaim;
import com.github.elrol.elrolsutilities.api.claims.IClaimSetting;
import com.github.elrol.elrolsutilities.api.claims.IClaimSettingEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Claim implements IClaim {
    private Map<String, IClaimSetting> claimSettings = new HashMap<>();
    private Map<ChunkPos, Map<String, IClaimSetting>> chunkSettings = new HashMap<>();

    public boolean isClaimed(BlockPos pos) {
        ChunkPos chunk = new ChunkPos(pos);
        return chunkSettings.containsKey(chunk);
    }

    public void claimChunk(ChunkPos pos) {
        chunkSettings.putIfAbsent(pos, new HashMap<>());
    }

    public void addClaimSetting(IClaimSettingEntry setting) {
        claimSettings.put(setting.getName(), setting.create(pos -> true));
    }

    public void addChunkSetting(BlockPos pos, IClaimSettingEntry setting) {
        final ChunkPos chunk = new ChunkPos(pos);
        Map<String, IClaimSetting> settings = chunkSettings.getOrDefault(chunk, new HashMap<>());
        settings.put(setting.getName(), setting.create(p -> true));
        chunkSettings.put(chunk, settings);
    }

}
