package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
            Main.textUtils.err(player, Errs.no_homes());
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(player, data), false);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
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
        IPlayerData data = Main.database.get(player.getUUID());
        if (data.getHomes().isEmpty()) {
            Main.textUtils.err(player, Errs.no_homes());
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
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
            Main.textUtils.msg(this.player, Msgs.validHomes.get(homes.toString()));
        }
    }

}

