package com.github.elrol.elrolsutilities.commands.permission;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class PermissionAdd {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("add")
        		.then(Commands.argument("player", EntityArgument.player())
        				.suggests(ModSuggestions::suggestPlayers)
        				.then(Commands.argument("perm", StringArgumentType.greedyString())
        						.suggests(ModSuggestions::suggestPerms)
        						.executes(PermissionAdd::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String perm = StringArgumentType.getString(c, "perm");
        ServerPlayer player = null;
        try {
            player = EntityArgument.getPlayer(c, "player");
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return 0;
        }
        if (perm.contains(" ")) {
            String s = "";
            for (String split : perm.split(" ")) {
                s = s.isEmpty() ? split : s + "_" + split;
            }
            perm = s;
        }
        UUID uuid = player.getUUID();
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

