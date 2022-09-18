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
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class KitCooldown {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("cooldown")
        		.then((Commands.argument("name", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, CommandConfig.kit_modify_perm.get()))
                        .executes(c -> KitCooldown.execute(c, StringArgumentType.getString(c, "name"), 0)))
        				.then(Commands.argument("cooldown", IntegerArgumentType.integer(0))
        						.executes(c -> KitCooldown.execute(c, StringArgumentType.getString(c, "name"), IntegerArgumentType.getInteger(c, "cooldown")))));
    }

    private static int execute(CommandContext<CommandSourceStack> c, String name, int cooldown) {
        if (Main.kitMap == null) {
            Logger.err("KitMap is null.");
            return 0;
        }
        if (!Main.kitMap.containsKey(name)) {
            Main.textUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Kit kit = Main.kitMap.get(name);
        if (cooldown == 0) {
            Main.textUtils.msg(c, Msgs.kit_cooldown_cleared.get(name));
        } else {
            Main.textUtils.msg(c, Msgs.kit_cooldown_set.get(String.valueOf(cooldown), name));
        }
        kit.cooldown = cooldown;
        kit.save();
        return 1;
    }
}

