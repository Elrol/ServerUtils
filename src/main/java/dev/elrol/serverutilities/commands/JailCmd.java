package dev.elrol.serverutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.data.JailData;
import dev.elrol.serverutilities.data.ServerData;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class JailCmd
extends _CmdBase {

    public JailCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.literal("create")
                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .executes(c -> create(c, StringArgumentType.getString(c,"name")))
                                )
                        )
                        .then(Commands.literal("delete")
                                .then(Commands.argument("jail", StringArgumentType.greedyString())
                                        .suggests(ModSuggestions::suggestJails)
                                        .executes(c -> delete(c, StringArgumentType.getString(c, "jail")))
                                )
                        )
                        .then(Commands.literal("cell")
                                .then(Commands.literal("add")
                                        .then(Commands.argument("jail", StringArgumentType.string())
                                                .suggests(ModSuggestions::suggestJails)
                                                .executes(c -> cellAdd(c, StringArgumentType.getString(c,"jail")))
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("jail", StringArgumentType.greedyString())
                                                .suggests(ModSuggestions::suggestJails)
                                                .then(Commands.argument("cell", IntegerArgumentType.integer(1))
                                                .executes(c -> cellRemove(c, StringArgumentType.getString(c,"jail"), IntegerArgumentType.getInteger(c, "cell"))))
                                        )
                                )
                        )
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("jail", StringArgumentType.string())
                                        .suggests(ModSuggestions::suggestJails)
                                        .then(Commands.argument("cell", IntegerArgumentType.integer(1))
                                                .then(Commands.argument("min", IntegerArgumentType.integer(1))
                                                        .executes(c -> jail(c, EntityArgument.getPlayer(c, "player"), StringArgumentType.getString(c,"jail"), IntegerArgumentType.getInteger(c, "cell"), IntegerArgumentType.getInteger(c, "min")))))
                                )
                        )
                );
        }
    }

    protected int jail(CommandContext<CommandSourceStack> c, ServerPlayer player, String jail, int cell, int min) {
        ServerData serverData = Main.serverData;
        JailData jailData = serverData.getJail(jail);
        if(player == null) {
            Main.textUtils.err(c.getSource(), Errs.player_missing());
            return 0;
        }
        if(jailData == null) {
            Main.textUtils.err(c.getSource(), Errs.jail_missing());
            return 0;
        }
        if(jailData.cells.size() < cell) {
            Main.textUtils.err(c.getSource(), Errs.cell_missing());
            return 0;
        }
        CommandDelay.init(this, c.getSource(), new JailCommandRunnable(c.getSource(), player, jail, cell -1, min), false);
        return 1;
    }

    protected int create(CommandContext<CommandSourceStack> c, String name) {
        if(Main.serverData.getJail(name) != null) {
            Main.textUtils.err(c.getSource(), Errs.jail_exists(name));
            return 0;
        }
        Main.serverData.createJail(name);
        Main.textUtils.msg(c.getSource(), Msgs.jail_created.get(name));
        return 1;
    }

    protected int delete(CommandContext<CommandSourceStack> c, String jail) {
        if(Main.serverData.getJail(jail) == null) {
            Main.textUtils.err(c, Errs.jail_missing());
        }
        Main.serverData.deleteJail(jail);
        Main.textUtils.msg(c.getSource(), Msgs.jail_deleted.get(name));
        return 1;
    }

    protected int cellAdd(CommandContext<CommandSourceStack> c, String name) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        Main.serverData.addJailCell(name, new Location(player));
        Main.textUtils.msg(c.getSource(), Msgs.cell_added.get(String.valueOf(Main.serverData.getJail(name).cells.size() - 1), name));
        return 1;
    }

    protected int cellRemove(CommandContext<CommandSourceStack> c, String name, int cell) {
        JailData jail = Main.serverData.getJail(name);
        if(jail.cells.size() > cell) {
            Main.textUtils.err(c.getSource(), Errs.cell_missing());
            return 0;
        }
        Main.serverData.removeJailCell(name, cell);
        Main.textUtils.msg(c.getSource(), Msgs.cell_removed.get(name, String.valueOf(cell)));
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{
                "Jail [player] [jail] [cell]: Sends the player to the specified cell in the Jail",
                "Jail create [name]: Creates a Jail with the name",
                "Jail delete [jail]: Deletes a Jail with the name",
                "Jail cell add [jail]: Adds a cell at the current location to the Jail",
                "Jail cell remove [jail] [cell]: Removes the cell from the Jail"
        };
        Main.textUtils.msgNoTag(c.getSource(), Main.textUtils.commandHelp(help));
        return 1;
    }

    private static class JailCommandRunnable implements Runnable {
        CommandSourceStack source;
        ServerPlayer player;
        String jail;
        int cell;
        int min;

        public JailCommandRunnable(CommandSourceStack source, ServerPlayer player, String jail, int cell, int min) {
            this.source = source;
            this.player = player;
            this.jail = jail;
            this.cell = cell;
            this.min = min;
        }

        @Override
        public void run() {
            IPlayerData data = Main.database.get(player.getUUID());
            Main.serverData.jail(player, jail, cell, min);
            Main.textUtils.msg(source, Msgs.jailed.get(data.getDisplayName(), jail, String.valueOf(cell)));
        }
    }
}

