package dev.elrol.serverutilities.data;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.CustomCommandTypes;
import dev.elrol.serverutilities.libs.Methods;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class CustomCommand {

    String name;
    int cooldown = 0;
    int delay = 0;
    List<String> cmds = new ArrayList<>();
    Map<String, CustomCommandTypes> argMap = new HashMap<>();

    public CustomCommand() {

    }

    public ArgumentBuilder<CommandSourceStack,?> getCommand() {
        ArgumentBuilder<CommandSourceStack, ?> cmd = Commands.literal(name).executes(this::execute);
        for (Map.Entry<String, CustomCommandTypes> entry : argMap.entrySet()) {
            switch (entry.getValue()) {
                case literal -> cmd = cmd.then(Commands.literal(entry.getKey()).executes(this::execute));
                case string -> cmd = cmd.then(Commands.argument(entry.getKey(), StringArgumentType.string()).executes(this::execute));
                case player -> cmd = cmd.then(Commands.argument(entry.getKey(), EntityArgument.players()).executes(this::execute));
                case number -> cmd = cmd.then(Commands.argument(entry.getKey(), IntegerArgumentType.integer()).executes(this::execute));
            };
        }
        cmd.executes(this::execute);
        return cmd;
    }

    private int execute(CommandContext<CommandSourceStack> c) {
        Map<String, String> args = new HashMap<>();
        for (String arg : argMap.keySet()) {
            CustomCommandTypes type = argMap.get(arg);
            String s = "";
            switch (type) {
                case literal -> { continue; }
                case string -> s = StringArgumentType.getString(c, arg);
                case number -> s = String.valueOf(IntegerArgumentType.getInteger(c, arg));
            }
            args.put(arg,s);
        }
        if(argMap.containsValue(CustomCommandTypes.player)) {
            AtomicReference<Set<String>> commands = new AtomicReference<>();
            cmds.forEach(cmd -> {
                final String[] command = {cmd};
                args.forEach((key,arg)-> command[0] = command[0].replace("{"+key+"}",arg));
                commands.get().add(command[0]);
            });
            argMap.forEach((k,v)->{
                if(v.equals(CustomCommandTypes.player)) {
                    Set<String> newCmds = new HashSet<>();
                    try {
                        Collection<ServerPlayer> players = EntityArgument.getPlayers(c,k);
                        players.forEach(player ->
                                commands.get().forEach(cmd ->
                                        newCmds.add(cmd.replace("{" + k + "}", player.getGameProfile().getName()))));
                        commands.set(newCmds);
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
            commands.get().forEach(cmd -> Methods.runCommand(c.getSource(),cmd));
        } else {
            cmds.forEach(cmd -> {
                final String[] command = {cmd};
                args.forEach((key,arg)-> command[0] = command[0].replace("{"+key+"}",arg));
                Methods.runCommand(c.getSource(), command[0]);
            });
        }
        return 1;
    }

}
