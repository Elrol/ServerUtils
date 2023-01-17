package dev.elrol.serverutilities.commands.kit;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.data.Kit;
import dev.elrol.serverutilities.init.CommandRegistry;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class KitGive {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("give")
        		.then(Commands.argument("kit", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .then(Commands.argument("players", EntityArgument.players())
                            .executes(c -> execute(c, StringArgumentType.getString(c, "kit"), EntityArgument.getPlayers(c, "players")))));
    }

    protected static int execute(CommandContext<CommandSourceStack> c, String kitname, Collection<ServerPlayer> players) {
        CommandSourceStack sender = c.getSource();
        ServerPlayer pSender;

        try {
            pSender = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            pSender = null;
        }

        if (!Main.kitMap.containsKey(kitname)) {
            Main.textUtils.err(sender, Errs.kit_doesnt_exist(kitname));
            return 0;
        }

        Kit kit = Main.kitMap.get(kitname);
        if (pSender != null && !IElrolAPI.getInstance().getPermissionHandler().hasPermission(pSender.getUUID(), kit.getPerm().get())) {
            Main.textUtils.err(c, Errs.no_permission());
            return 0;
        }

        CommandRunnable runable = new CommandRunnable(pSender, players, kit);

        if(pSender == null) runable.run();
        else CommandDelay.init(CommandRegistry.kitCmd, pSender, runable, false);
        return 1;
    }


    private static class CommandRunnable implements Runnable {
        Kit kit;
        ServerPlayer sender;
        Collection<ServerPlayer> players;

        public CommandRunnable(ServerPlayer sender, Collection<ServerPlayer> players, Kit kit) {
            this.kit = kit;
            this.sender = sender;
            this.players = players;
        }

        @Override
        public void run() {
            players.forEach(player -> {
                kit.give(player);
                Main.textUtils.msg(player, Msgs.received_kit.get(kit.name));
                Main.getLogger().info(player.getDisplayName() + " claimed Kit " + kit.name + (kit.getCost() > 0 ? " for " + Main.textUtils.parseCurrency(kit.getCost(), true) : ""));
            });
            Main.textUtils.msg(sender, Msgs.sent_kit.get(kit.name, String.valueOf(players.size())));
        }
    }
}

