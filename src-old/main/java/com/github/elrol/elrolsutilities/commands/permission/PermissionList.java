/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.Command
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.ArgumentBuilder
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  net.minecraft.command.CommandSource
 *  net.minecraft.command.Commands
 */
package com.github.elrol.elrolsutilities.commands.permission;

import java.util.ArrayList;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class PermissionList {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return (Commands.literal("list")
        		.executes(PermissionList::execute))
        		.then(Commands.argument("filter", StringArgumentType.string())
        				.executes(c -> PermissionList.execute(c, StringArgumentType.getString(c, "filter"))));
    }

    private static int execute(CommandContext<CommandSource> c, String filter) {
        ArrayList<String> filtered = new ArrayList<String>();
        for (String perm : Main.permRegistry.commandPerms.values()) {
            if (perm.startsWith(filter)) {
                filtered.add(perm);
                continue;
            }
            Logger.debug("Filter did not match: " + perm);
        }
        TextUtils.msg(c, Msgs.permission_list());
        for (String p : filtered) {
            TextUtils.tab_msg(c.getSource(), p);
        }
        return 0;
    }

    private static int execute(CommandContext<CommandSource> c) {
        TextUtils.msg(c, Msgs.permission_list());
        for (String p : Main.permRegistry.commandPerms.values()) {
            TextUtils.tab_msg(c.getSource(), p);
        }
        return 1;
    }
}

