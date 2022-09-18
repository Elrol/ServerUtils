package dev.elrol.serverutilities.data;

import dev.elrol.serverutilities.libs.CustomCommandTypes;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public class CustomCommandArg {

    CustomCommandTypes type;
    String name;

    public ArgumentBuilder<CommandSourceStack,?> getCommand() {
        return switch (type) {
            case literal -> Commands.literal(name).executes(this::execute);
            case string -> Commands.argument(name, StringArgumentType.string()).executes(this::execute);
            case player -> Commands.argument(name, EntityArgument.players()).executes(this::execute);
            case number -> Commands.argument(name, IntegerArgumentType.integer()).executes(this::execute);
        };
    }

    private int execute(CommandContext<CommandSourceStack> c) {
        return 0;
    }

}
