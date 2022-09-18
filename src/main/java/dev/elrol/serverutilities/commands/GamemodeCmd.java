package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class GamemodeCmd
extends _CmdBase {
    public GamemodeCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .then(Commands.argument("gamemode", StringArgumentType.string())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.asList("0", "survival", "1", "creative", "2", "adventure", "3", "spectator"), builder))
                                .executes(this::execute)));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        String gamemode;
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
            gamemode = StringArgumentType.getString(c, "gamemode").toLowerCase();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(c.getSource(), player, gamemode), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        ServerPlayer player;
        String gamemode;

        public CommandRunnable(CommandSourceStack source, ServerPlayer player, String gamemode) {
            this.source = source;
            this.player = player;
            this.gamemode = gamemode;
        }

        @Override
        public void run() {
            GameType type;
            switch (this.gamemode) {
                case "1": 
                case "creative": {
                    type = GameType.CREATIVE;
                    break;
                }
                case "2": 
                case "adventure": {
                    type = GameType.ADVENTURE;
                    break;
                }
                case "3": 
                case "spectator": {
                    type = GameType.SPECTATOR;
                    break;
                }
                default: {
                    type = GameType.SURVIVAL;
                }
            }
            if (source.getTextName().equalsIgnoreCase(player.getName().getString())) {
                source.sendSuccess(Component.translatable("commands.gamemode.success.self", type.getName()), true);
            } else {
                source.sendSuccess(Component.translatable("commands.gamemode.success.other", this.player.getName(), type.getName()), true);
            }
            player.setGameMode(type);
        }
    }

}

