/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.Command
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.ArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.suggestion.SuggestionProvider
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.command.CommandSource
 *  net.minecraft.command.Commands
 */
package com.github.elrol.elrolsutilities.commands.permission;

import java.util.UUID;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class PermissionRemove {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("remove")
        		.then(Commands.argument("player", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestPlayers)
        				.then(Commands.argument("perm", StringArgumentType.greedyString())
        						.suggests(ModSuggestions::suggestPerms)
        						.executes(PermissionRemove::execute)));
    }

    private static int execute(CommandContext<CommandSource> c) {
        UUID uuid;
        String perm = StringArgumentType.getString(c, "perm");
        String name = StringArgumentType.getString(c, "player");
        if (perm.contains(" ")) {
            String s = "";
            for (String split : perm.split(" ")) {
                s = s.isEmpty() ? split : s + "_" + split;
            }
            perm = s;
        }
        if ((uuid = Methods.getUUIDFromName(name)) == null) {
            TextUtils.err(c, Errs.player_not_found(name));
            return 0;
        }
        PlayerData data = Main.database.get(uuid);
        if (perm.isEmpty()) {
            TextUtils.err(c, Errs.empty_perm());
            return 0;
        }
        if (!data.hasPerm(perm)) {
            TextUtils.err(c, Errs.missing_perm(data.getDisplayName(), perm));
            return 0;
        }
        data.removePerm(perm);
        Main.database.save(uuid);
        data.update();
        TextUtils.msg(c, Msgs.removed_perm(perm, data.getDisplayName()));
        return 1;
    }
}

