package com.github.elrol.elrolsutilities.data.claimSettings.entries;

import com.github.elrol.elrolsutilities.api.claims.IClaimSetting;
import com.github.elrol.elrolsutilities.api.claims.IClaimSettingEntry;
import com.github.elrol.elrolsutilities.data.claimSettings.EntryClaimSetting;
import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public class EntryClaimSettingEntry implements IClaimSettingEntry {

    String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IClaimSetting create(Function<BlockPos, Boolean> delegate) {
        return new EntryClaimSetting(name, delegate);
    }

    @Override
    public IClaimSetting load(String json) {
        return null;
    }
}
