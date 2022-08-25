package com.github.elrol.elrolsutilities.commands.permission;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import java.util.UUID;

public class PermissionCheck {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("check")
        		.then(Commands.argument("player", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestPlayers)
        				.executes(PermissionCheck::execute));
    }

    private static int execute(CommandContext<CommandSource> c) {
        String name = StringArgumentType.getString(c, "player");
        UUID uuid = Methods.getUUIDFromName(name);
        if (uuid == null) {
            TextUtils.err(c, Errs.player_not_found(name));
            return 0;
        }
        IPlayerData data = Main.database.get(uuid);
        TextUtils.msg(c, Msgs.permissionOther.get(data.getDisplayName()));
        for (String p : data.getPerms()) {
            c.getSource().sendSuccess(new StringTextComponent(p), true);
        }
        return 1;
    }
}

