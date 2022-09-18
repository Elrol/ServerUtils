package dev.elrol.serverutilities.commands.permission;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.UUID;

public class PermissionClear {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("clear")
        		.then(Commands.argument("player", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestPlayers)
        				.executes(PermissionClear::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String name = StringArgumentType.getString(c, "player");
        UUID uuid = Methods.getUUIDFromName(name);
        if (uuid == null) {
            Main.textUtils.err(c, Errs.player_not_found(name));
            return 0;
        }
        IPlayerData data = Main.database.get(uuid);
        data.clearPerms();
        Main.database.save(uuid);
        data.update();
        Main.textUtils.msg(c, Msgs.clearedPerms.get(data.getDisplayName()));
        return 1;
    }
}

