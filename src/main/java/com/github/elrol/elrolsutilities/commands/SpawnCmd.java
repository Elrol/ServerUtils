package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class SpawnCmd
        extends _CmdBase {
    public SpawnCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a).executes(this::execute));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        Location newLoc;
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        if (Main.serverData.spawnPoint != null) {
            if(Main.serverData.spawnPoint.world.equals(new ResourceLocation("minecraft", "dimension"))) {
                BlockPos spawn = Main.mcServer.overworld().getSharedSpawnPos();
                newLoc = new Location(Main.mcServer.overworld().dimension(), spawn, 0.0f, 0.0f);
                Main.serverData.setSpawn(newLoc);
            } else {
                newLoc = Main.serverData.spawnPoint;
            }
        } else {
            BlockPos spawn = Main.mcServer.overworld().getSharedSpawnPos();
            newLoc = new Location(Main.mcServer.overworld().dimension(), spawn, 0.0f, 0.0f);
        }
        Location loc = Methods.getPlayerLocation(player);
        PlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, loc, newLoc), true);
        return 1;
    }

    private static class CommandRunnable
            implements Runnable {
        ServerPlayerEntity player;
        Location loc;
        Location newLoc;

        public CommandRunnable(ServerPlayerEntity player, Location loc, Location newLoc) {
            this.player = player;
            this.loc = loc;
            this.newLoc = newLoc;
        }

        @Override
        public void run() {
            if(Methods.teleport(player, loc, newLoc))
                TextUtils.msg(player, Msgs.spawn());
        }
    }

}
