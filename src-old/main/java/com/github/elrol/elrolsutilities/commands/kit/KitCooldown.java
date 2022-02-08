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
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class KitCooldown {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("cooldown")
        		.then((Commands.argument("name", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, CommandConfig.kit_modify_perm.get()))
                        .executes(c -> KitCooldown.execute(c, StringArgumentType.getString(c, "name"), 0)))
        				.then(Commands.argument("cooldown", IntegerArgumentType.integer(0))
        						.executes(c -> KitCooldown.execute(c, StringArgumentType.getString(c, "name"), IntegerArgumentType.getInteger(c, "cooldown")))));
    }

    private static int execute(CommandContext<CommandSource> c, String name, int cooldown) {
        if (Main.kitMap == null) {
            Logger.err("KitMap is null.");
            return 0;
        }
        if (!Main.kitMap.containsKey(name)) {
            TextUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Kit kit = Main.kitMap.get(name);
        if (cooldown == 0) {
            TextUtils.msg(c, Msgs.kit_cooldown_cleared(name));
        } else {
            TextUtils.msg(c, Msgs.kit_cooldown_set(cooldown, name));
        }
        kit.cooldown = cooldown;
        kit.save();
        return 1;
    }
}

