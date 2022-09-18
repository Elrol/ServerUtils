package dev.elrol.serverutilities.data.claimSettings.entries;

import dev.elrol.serverutilities.api.claims.IClaimSetting;
import dev.elrol.serverutilities.api.claims.IClaimSettingEntry;
import dev.elrol.serverutilities.data.claimSettings.EntryClaimSetting;
import net.minecraft.core.BlockPos;

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
