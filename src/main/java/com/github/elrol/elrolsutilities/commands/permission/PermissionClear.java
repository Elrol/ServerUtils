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

public class PermissionClear {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("clear")
        		.then(Commands.argument("player", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestPlayers)
        				.executes(PermissionClear::execute));
    }

    private static int execute(CommandContext<CommandSource> c) {
        String name = StringArgumentType.getString(c, "player");
        UUID uuid = Methods.getUUIDFromName(name);
        if (uuid == null) {
            TextUtils.err(c, Errs.player_not_found(name));
            return 0;
        }
        PlayerData data = Main.database.get(uuid);
        data.clearPerms();
        Main.database.save(uuid);
        data.update();
        TextUtils.msg(c, Msgs.cleared_perms(data.getDisplayName()));
        return 1;
    }
}

