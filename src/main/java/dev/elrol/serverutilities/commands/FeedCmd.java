package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FeedCmd
extends _CmdBase {

    //private static final Field saturationLevel = ObfuscationReflectionHelper.findField(FoodStats.class, "saturationLevel");

    public FeedCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(c -> execute(c, EntityArgument.getPlayers(c, "players")))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, Collection<ServerPlayer> players) {
        ServerPlayer sender;
        try {
            sender = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            sender = null;
        }
        if(sender != null) {
            IPlayerData data = Main.database.get(sender.getUUID());
            if (FeatureConfig.enable_economy.get() && this.cost > 0) {
                if (!data.charge(this.cost)) {
                    Main.textUtils.err(sender, Errs.not_enough_funds(this.cost, data.getBal()));
                    return 0;
                }
            }
        }
        CommandDelay.init(this, sender, new CommandRunnable(c.getSource(), players), false);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        return execute(c, Collections.singleton(player));
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        Collection<ServerPlayer> players;

        public CommandRunnable(CommandSourceStack source, Collection<ServerPlayer> players) {
            this.source = source;
            this.players = players;
        }

        @Override
        public void run() {
            players.forEach(player -> {
                if (this.source.getTextName().equalsIgnoreCase(player.getName().getString())) {
                    Main.textUtils.msg(this.source, Msgs.fed_self.get());
                } else {
                    Main.textUtils.msg(this.source, Msgs.feed.get(Methods.getDisplayName(player)));
                    Main.textUtils.msg(player, Msgs.fed.get());
                }
                player.getFoodData().setFoodLevel(20);
                /**try {
                    saturationLevel.set(player.getFoodData(), 20.0f);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                 **/
            });
        }
    }

}

