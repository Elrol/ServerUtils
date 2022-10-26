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

public class HomeCmd
extends _CmdBase {
    public HomeCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("name", StringArgumentType.string())
                                .suggests(ModSuggestions::suggestHomes)
                                .executes(c -> this.execute(c, StringArgumentType.getString(c, "name")))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, String name) {
        ServerPlayer player;
        if (name.isEmpty()) {
            name = "Home";
        }
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, name)) {
            return 0;
        }
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        name = data.getHome(name);
        if (!data.getHomes().containsKey(name)) {
            Main.textUtils.err(player, Errs.home_not_found(name));
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        Location loc = new Location(player);
        Location home = data.getHomes().get(name);
        CommandDelay.init(this, player, new CommandRunnable(player, loc, home, name), true);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        return this.execute(c, "Home");
    }

    private static class CommandRunnable
            implements Runnable {
        ServerPlayer player;
        String home;
        Location loc;
        Location newLoc;

        public CommandRunnable(ServerPlayer player, Location loc, Location newLoc, String home) {
            this.player = player;
            this.home = home;
            this.loc = loc;
            this.newLoc = newLoc;
        }

        @Override
        public void run() {
            if (Methods.teleport(player, loc, newLoc))
                Main.textUtils.msg(this.player, Msgs.welcomeHome.get(this.home));
        }
    }
}