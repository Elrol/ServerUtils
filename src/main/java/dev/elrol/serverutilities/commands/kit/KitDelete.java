package dev.elrol.serverutilities.commands.kit;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class KitDelete {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("delete")
        		.then(Commands.argument("name", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, CommandConfig.kit_modify_perm.get()))
                        .executes(c -> KitDelete.execute(c, StringArgumentType.getString(c, "name"))));
    }

    private static int execute(CommandContext<CommandSourceStack> c, String name) {
        if (!Main.kitMap.containsKey(name)) {
            Main.textUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Main.kitMap.remove(name);
        Main.textUtils.msg(c, Msgs.kit_deleted.get(name));
        return 1;
    }
}

