package dev.elrol.serverutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class SpawnCmd
        extends _CmdBase {
    public SpawnCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a).executes(this::execute));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        Location newLoc;
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
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
        Location loc = new Location(player);
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, loc, newLoc), true);
        return 1;
    }

    private static class CommandRunnable
            implements Runnable {
        ServerPlayer player;
        Location loc;
        Location newLoc;

        public CommandRunnable(ServerPlayer player, Location loc, Location newLoc) {
            this.player = player;
            this.loc = loc;
            this.newLoc = newLoc;
        }

        @Override
        public void run() {
            if(Methods.teleport(player, loc, newLoc))
                Main.textUtils.msg(player, Msgs.spawn.get());
        }
    }

}
