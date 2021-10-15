package com.github.elrol.elrolsutilities.data.claimSettings;

import com.github.elrol.elrolsutilities.api.claims.IClaimSetting;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Function;

public class ExampleClaimSetting implements IClaimSetting {

    private String claim;
    private boolean flag;
    private String name;
    private Function<BlockPos, Boolean> delegate;

    public ExampleClaimSetting(final String claim, String name) {
        this.claim = claim;
        this.name = name;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> claimSettingBuilder(LiteralArgumentBuilder<CommandSource> builder) {
        return builder.then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> set(c, BoolArgumentType.getBool(c, "value"))));
    }

    @Override
    public String getName() {
        return name;
    }

    public int set(CommandContext<CommandSource> c, boolean isChunkSetting) {
        flag = BoolArgumentType.getBool(c, "value");
        return 0;
    }

    @SubscribeEvent
    public void interactionEvent(PlayerInteractEvent event) {

    }
}
