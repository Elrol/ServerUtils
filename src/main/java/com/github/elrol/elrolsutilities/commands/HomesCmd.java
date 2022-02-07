package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class HomesCmd
extends _CmdBase {
    public HomesCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .executes(this::execute)
                        .then(Commands.argument("player", EntityArgument.player())
                                .requires(c -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(c, CommandConfig.homes_other.get()))
                                .executes(c -> other(c, EntityArgument.getPlayer(c,"player")))
                        )
                );
        }
    }

    protected int other(CommandContext<CommandSourceStack> c, ServerPlayer player) {
        IPlayerData data = IElrolAPI.getInstance().getPlayerDatabase().get(player.getUUID());
        if (data.getHomes().isEmpty()) {
            TextUtils.err(player, Errs.no_homes());
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                TextUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(player, data), false);
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
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
        IPlayerData data = Main.database.get(player.getUUID());
        if (data.getHomes().isEmpty()) {
            TextUtils.err(player, Errs.no_homes());
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, data), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer player;
        IPlayerData data;

        public CommandRunnable(ServerPlayer player, IPlayerData data) {
            this.player = player;
            this.data = data;
        }

        @Override
        public void run() {
            StringBuilder homes = new StringBuilder();
            data.getHomes().forEach((name, loc) -> {
                BlockPos pos = loc.getBlockPos();
                name = "&a" + name;
                String coords = " &8[&7" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "&8]";
                homes.append(name).append(coords).append("\n");
            });
            TextUtils.msg(this.player, Msgs.valid_homes(homes.toString()));
        }
    }

}

