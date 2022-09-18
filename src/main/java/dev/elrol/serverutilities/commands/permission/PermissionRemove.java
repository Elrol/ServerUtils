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

public class PermissionRemove {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("remove")
        		.then(Commands.argument("player", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestPlayers)
        				.then(Commands.argument("perm", StringArgumentType.greedyString())
        						.suggests(ModSuggestions::suggestPerms)
        						.executes(PermissionRemove::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
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
            Main.textUtils.err(c, Errs.player_not_found(name));
            return 0;
        }
        IPlayerData data = Main.database.get(uuid);
        if (perm.isEmpty()) {
            Main.textUtils.err(c, Errs.empty_perm());
            return 0;
        }
        if (!data.hasPerm(perm)) {
            Main.textUtils.err(c, Errs.missing_perm(data.getDisplayName(), perm));
            return 0;
        }
        data.removePerm(perm);
        Main.database.save(uuid);
        data.update();
        Main.textUtils.msg(c, Msgs.removed_perm.get(perm, data.getDisplayName()));
        return 1;
    }
}

