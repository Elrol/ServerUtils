package dev.elrol.serverutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class WarpCmd
extends _CmdBase {
    public WarpCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .then(Commands.argument("name", StringArgumentType.string())
                                .suggests(ModSuggestions::suggestWarps)
                                .executes(this::execute)));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        String warp = StringArgumentType.getString(c, "name").toLowerCase();
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
        if (Main.serverData.getWarp(warp) == null) {
            Main.textUtils.err(player, Errs.warp_not_found(warp));
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());

        if(!data.hasPermOrOp("serverutils.warp." + warp)) {
            Main.textUtils.err(player, Errs.no_perms_for_warp.get(warp));
            return 0;
        }

        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, new Location(player), warp), true);
        return 1;
    }

    private static class CommandRunnable
            implements Runnable {
        ServerPlayer player;
        Location loc;
        String warp;

        public CommandRunnable(ServerPlayer player, Location loc, String warp) {
            this.player = player;
            this.warp = warp;
            this.loc = loc;
        }

        @Override
        public void run() {
            if(Methods.teleport(player, loc, Main.serverData.getWarp(warp)))
                Main.textUtils.msg(this.player, Msgs.warpWelcome.get(this.warp));
        }
    }

}


