package com.github.elrol.elrolsutilities.commands.kit;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class KitDelete {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("delete")
        		.then(Commands.argument("name", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, CommandConfig.kit_modify_perm.get()))
                        .executes(c -> KitDelete.execute(c, StringArgumentType.getString(c, "name"))));
    }

    private static int execute(CommandContext<CommandSource> c, String name) {
        if (!Main.kitMap.containsKey(name)) {
            TextUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Main.kitMap.remove(name);
        TextUtils.msg(c, Msgs.kit_deleted.get(name));
        return 1;
    }
}

