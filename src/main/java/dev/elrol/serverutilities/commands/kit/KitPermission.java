package dev.elrol.serverutilities.commands.kit;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.data.Kit;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
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
            Main.textUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Kit kit = Main.kitMap.get(name);
        Main.textUtils.msg(c, Msgs.kit_perms.get(name, kit.getPerm().get()));
        return 1;
    }
}

