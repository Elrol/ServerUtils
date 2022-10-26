package dev.elrol.serverutilities.api.claims;

import net.minecraft.core.BlockPos;

import java.util.function.Function;

public interface IClaimSettingEntry {

    String getName();
    IClaimSetting create(Function<BlockPos, Boolean> delegate);
    IClaimSetting load(String json);

}
