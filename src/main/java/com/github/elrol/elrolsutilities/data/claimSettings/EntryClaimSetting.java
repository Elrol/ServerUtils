package com.github.elrol.elrolsutilities.data.claimSettings;

import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.claims.IClaim;
import com.github.elrol.elrolsutilities.api.claims.IClaimSetting;
import com.github.elrol.elrolsutilities.api.claims.IClaimSettingEntry;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntryClaimSetting implements IClaimSetting {
    String name;
    boolean flag;
    private Function<BlockPos, Boolean> delegate;

    private Map<String, IClaimSetting> settings = new HashMap<>();

    public EntryClaimSetting(String name, Function<BlockPos, Boolean> delegate) {
        this.delegate = delegate;
        this.name = name;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @SubscribeEvent
    public void interactionEvent(PlayerInteractEvent event) {
        //stuff
    }

    @Override
    public int set(CommandContext<CommandSourceStack> c, boolean isChunkSetting) {
        flag = BoolArgumentType.getBool(c, "flag");
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        IElrolAPI api = IElrolAPI.getInstance();
        IClaim claim = api.getClaimManager().getOrCreate(player.getUUID());
        IClaimSettingEntry entry = api.getClaimSettingRegistry().get(name);

        if(isChunkSetting) {
            claim.addChunkSetting(player.blockPosition(), entry);
        } else {
            claim.addClaimSetting(entry);
        }

        return 0;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> claimSettingBuilder(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder
                .then(Commands.literal("claim")
                        .then(Commands.literal(name)
                                .then(Commands.argument("flag", BoolArgumentType.bool())
                                        .executes(c -> set(c, true)))))
                .then(Commands.literal(name)
                        .then(Commands.argument("flag", BoolArgumentType.bool())
                                .executes(c -> set(c, false))));
    }
}
