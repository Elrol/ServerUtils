package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.CommandSource;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public class CommandUpdater {

    static Field requirementField;

    public static void init(CommandDispatcher<CommandSource> dispatcher) throws NoSuchFieldException {
        requirementField = CommandNode.class.getDeclaredField("requirement");
        requirementField.setAccessible(true);

        Collection<CommandNode<CommandSource>> commands = dispatcher.getRoot().getChildren();
        for(CommandNode<CommandSource> command : commands) {
            Logger.debug("Updating Command: " + command.getName());
            if(command.getName().equalsIgnoreCase("spark")) continue;
            checkCommand(command, "");
        }
    }

    private static void setPermission(CommandNode<CommandSource> command, String node, String perm){
        try {
            requirementField.set(command, (Predicate<CommandSource>) source -> checkPermission(source, node, perm));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void checkCommand(CommandNode<CommandSource> command, String node) {
        String perm = "";

        CommandNode<CommandSource> redirect = command.getRedirect();
        if(redirect != null) {
            node += redirect.getName();
        } else {
            node += command.getName();
        }

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

    private static boolean checkPermission(CommandSource source, String node, String perm) {
        return IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, node, perm);
    }

}
