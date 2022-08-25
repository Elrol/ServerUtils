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

import java.util.UUID;

public class PermissionAdd {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("add")
        		.then(Commands.argument("player", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestPlayers)
        				.then(Commands.argument("perm", StringArgumentType.greedyString())
        						.suggests(ModSuggestions::suggestPerms)
        						.executes(PermissionAdd::execute)));
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
        IPlayerData data = Main.database.get(uuid);
        if (perm.isEmpty()) {
            TextUtils.err(c, Errs.empty_perm());
            return 0;
        }
        if (data.hasPerm(perm)) {
            TextUtils.err(c, Errs.dupe_perm(data.getDisplayName(), perm));
            return 0;
        }
        data.addPerm(perm);
        Main.database.save(uuid);
        data.update();
        TextUtils.msg(c, Msgs.addedPerm.get(perm, data.getDisplayName()));
        return 1;
    }
}

