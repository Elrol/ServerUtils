package dev.elrol.serverutilities.data.claimSettings;

import dev.elrol.serverutilities.api.claims.IClaimSetting;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
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
    public LiteralArgumentBuilder<CommandSourceStack> claimSettingBuilder(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.then(Commands.argument("value", BoolArgumentType.bool()).executes(c -> set(c, BoolArgumentType.getBool(c, "value"))));
    }

    @Override
    public String getName() {
        return name;
    }

    public int set(CommandContext<CommandSourceStack> c, boolean isChunkSetting) {
        flag = BoolArgumentType.getBool(c, "value");
        return 0;
    }

    @SubscribeEvent
    public void interactionEvent(PlayerInteractEvent event) {

    }
}
