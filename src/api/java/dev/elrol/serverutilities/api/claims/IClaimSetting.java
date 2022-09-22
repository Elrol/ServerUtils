package dev.elrol.serverutilities.api.claims;


import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public interface IClaimSetting {

    String getName();
    int set(CommandContext<CommandSourceStack> c, boolean isChunkSetting);
    LiteralArgumentBuilder<CommandSourceStack> claimSettingBuilder(LiteralArgumentBuilder<CommandSourceStack> builder);
    default void loadJson(String json){}
}
