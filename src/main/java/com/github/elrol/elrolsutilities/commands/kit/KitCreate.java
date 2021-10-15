package com.github.elrol.elrolsutilities.commands.kit;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.data.Kit;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class KitCreate {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("create")
        		.then((Commands.argument("name", StringArgumentType.string())
        				.requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, CommandConfig.kit_modify_perm.get())))
        				.executes(c -> KitCreate.execute(c, StringArgumentType.getString(c, "name"))));
    }

    private static int execute(CommandContext<CommandSource> c, String name) {
        if (Main.kitMap == null) {
            Logger.err("KitMap is null.");
            return 0;
        }
        if (Main.kitMap.containsKey(name)) {
            TextUtils.err(c, Errs.kit_exists(name));
            return 0;
        }
        Kit kit = new Kit(name);
        kit.save();
        Main.kitMap.put(name, kit);
        TextUtils.msg(c, Msgs.kit_created(name));
        return 1;
    }
}

