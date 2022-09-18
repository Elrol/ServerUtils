package dev.elrol.serverutilities.init;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.libs.Logger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public class CommandUpdater {

    static Field requirementField;

    public static void init(CommandDispatcher<CommandSourceStack> dispatcher) throws NoSuchFieldException {
        requirementField = CommandNode.class.getDeclaredField("requirement");
        requirementField.setAccessible(true);

        Collection<CommandNode<CommandSourceStack>> commands = dispatcher.getRoot().getChildren();
        for(CommandNode<CommandSourceStack> command : commands) {
            Logger.debug("Updating Command: " + command.getName());
            if(command.getName().equalsIgnoreCase("spark")) continue;
            checkCommand(command, "");
        }
    }

    private static void setPermission(CommandNode<CommandSourceStack> command, String node, String perm){
        try {
            requirementField.set(command, (Predicate<CommandSourceStack>) source -> checkPermission(source, node, perm));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void checkCommand(CommandNode<CommandSourceStack> command, String node) {
        String perm = "";

        CommandNode<CommandSourceStack> redirect = command.getRedirect();
        node += Objects.requireNonNullElse(redirect, command).getName();

        if (Main.permRegistry.commandPerms.containsKey(node)) {
            perm = Main.permRegistry.commandPerms.get(node);
        } else {
            perm = "command." + node.replace("_", ".");
            Main.permRegistry.add(node, perm);
        }
        setPermission(command, node, perm);

        String finalNode = node + "_";
        command.getChildren().forEach(c -> checkCommand(c, finalNode));
    }

    private static boolean checkPermission(CommandSourceStack source, String node, String perm) {
        return IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, node, perm);
    }

}
