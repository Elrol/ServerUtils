package dev.elrol.serverutilities.api.claims;

import net.minecraft.core.BlockPos;

import java.util.function.Function;

public interface IClaimSettingEntry {

    String getName();
    dev.elrol.serverutilities.api.claims.IClaimSetting create(Function<BlockPos, Boolean> delegate);
    dev.elrol.serverutilities.api.claims.IClaimSetting load(String json);

}
