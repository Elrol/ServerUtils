package com.github.elrol.elrolsutilities.data.claimSettings;

import com.github.elrol.elrolsutilities.api.claims.IClaimSetting;
import com.github.elrol.elrolsutilities.api.claims.IClaimSettingEntry;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public class ClaimEntrySetting implements IClaimSetting {


    @Override
    public String getName() {
        return null;
    }

    @Override
    public int set(CommandContext<CommandSource> c, boolean isChunkSetting) {
        return 0;
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> claimSettingBuilder(LiteralArgumentBuilder<CommandSource> builder) {
        return null;
    }
}
