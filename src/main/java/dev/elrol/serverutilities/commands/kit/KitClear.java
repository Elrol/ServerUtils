package dev.elrol.serverutilities.commands.kit;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.data.Kit;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class KitClear {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("clear")
        		.then(Commands.argument("kit", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, CommandConfig.kit_modify_perm.get()))
                        .executes(c -> execute(c, StringArgumentType.getString(c, "kit"))));
    }

    private static int execute(CommandContext<CommandSourceStack> c, String name) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        if (!Main.kitMap.containsKey(name)) {
            Main.textUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Kit kit = Main.kitMap.get(name);
        kit.clearKit();
        Main.textUtils.msg(player, Msgs.kit_cleared.get(name));
        return 0;
    }
}

