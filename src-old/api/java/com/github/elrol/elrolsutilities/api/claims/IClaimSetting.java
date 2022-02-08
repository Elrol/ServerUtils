package com.github.elrol.elrolsutilities.api.claims;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;

public interface IClaimSetting {

    String getName();
    int set(CommandContext<CommandSource> c, boolean isChunkSetting);
    LiteralArgumentBuilder<CommandSource> claimSettingBuilder(LiteralArgumentBuilder<CommandSource> builder);
    default void loadJson(String json){}
}
