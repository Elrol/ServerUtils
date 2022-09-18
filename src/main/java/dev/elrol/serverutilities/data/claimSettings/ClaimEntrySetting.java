package dev.elrol.serverutilities.data.claimSettings;

import dev.elrol.serverutilities.api.claims.IClaimSetting;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class ClaimEntrySetting implements IClaimSetting {


    @Override
    public String getName() {
        return null;
    }

    @Override
    public int set(CommandContext<CommandSourceStack> c, boolean isChunkSetting) {
        return 0;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> claimSettingBuilder(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return null;
    }
}
