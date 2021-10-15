package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.ClaimBlock;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.UUID;

public class UnclaimCmd extends _CmdBase {
    public UnclaimCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute)));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        ClaimBlock claim = new ClaimBlock(player);
        PlayerData data = Main.database.get(player.getUUID());
        if (Main.serverData.isClaimed(claim)) {
            UUID uuid = Main.serverData.getOwner(claim);
            if(uuid.equals(player.getUUID()) || data.bypass){
                if (FeatureConfig.enable_economy.get() && this.cost > 0) {
                    if (!data.charge(this.cost)) {
                        TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                        return 0;
                    }
                }
                CommandDelay.init(this, player, new CommandRunnable(player), false);
                return 1;
            } else {
                PlayerData d = Main.database.get(uuid);
                TextUtils.err(player, Errs.chunk_not_yours(d.getDisplayName()));
                return 0;
            }
        } else {
            TextUtils.err(player, Errs.chunk_not_claimed());
            return 0;
        }
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity player;

        public CommandRunnable(ServerPlayerEntity target) {
            this.player = target;
        }

        @Override
        public void run() {
            TextUtils.msg(this.player, Msgs.chunk_unclaimed());
            Main.serverData.unclaim(new ClaimBlock(player));
        }
    }

}

