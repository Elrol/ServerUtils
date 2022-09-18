package dev.elrol.serverutilities.data.claimSettings;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.claims.IClaim;
import dev.elrol.serverutilities.api.claims.IClaimSetting;
import dev.elrol.serverutilities.api.claims.IClaimSettingEntry;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.TextUtils;
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
            Main.textUtils.err(c.getSource(), Errs.not_player());
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
