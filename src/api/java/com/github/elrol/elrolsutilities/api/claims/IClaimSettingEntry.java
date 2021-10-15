package com.github.elrol.elrolsutilities.api.claims;

import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public interface IClaimSettingEntry {

    String getName();
    IClaimSetting create(Function<BlockPos, Boolean> delegate);
    IClaimSetting load(String json);

}
