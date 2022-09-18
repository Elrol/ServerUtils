package dev.elrol.serverutilities.commands.permission;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;

public class PermissionList {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (Commands.literal("list")
        		.executes(PermissionList::execute))
        		.then(Commands.argument("filter", StringArgumentType.string())
        				.executes(c -> PermissionList.execute(c, StringArgumentType.getString(c, "filter"))));
    }

    private static int execute(CommandContext<CommandSourceStack> c, String filter) {
        ArrayList<String> filtered = new ArrayList<String>();
        for (String perm : Main.permRegistry.commandPerms.values()) {
            if (perm.startsWith(filter)) {
                filtered.add(perm);
                continue;
            }
            Logger.debug("Filter did not match: " + perm);
        }
        Main.textUtils.msg(c, Msgs.permission_list.get());
        for (String p : filtered) {
            Main.textUtils.tab_msg(c.getSource(), p);
        }
        return 0;
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        Main.textUtils.msg(c, Msgs.permission_list.get());
        for (String p : Main.permRegistry.commandPerms.values()) {
            Main.textUtils.tab_msg(c.getSource(), p);
        }
        return 1;
    }
}

