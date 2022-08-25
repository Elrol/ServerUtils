package com.github.elrol.elrolsutilities.commands.kit;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.data.Kit;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class KitPermission {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("perm")
        		.then(Commands.argument("name", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, CommandConfig.kit_modify_perm.get()))
                        .executes(c -> KitPermission.execute(c, StringArgumentType.getString(c, "name"))));
    }

    private static int execute(CommandContext<CommandSourceStack> c, String name) {
        if (Main.kitMap == null) {
            Logger.err("KitMap is null.");
            return 0;
        }
        if (!Main.kitMap.containsKey(name)) {
            TextUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Kit kit = Main.kitMap.get(name);
        TextUtils.msg(c, Msgs.kit_perms.get(name, kit.getPerm().get()));
        return 1;
    }
}

