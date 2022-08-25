package com.github.elrol.elrolsutilities.commands.kit;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.data.Kit;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

public class KitAdd {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("add")
        		.then(Commands.argument("kit", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, CommandConfig.kit_modify_perm.get()))
                        .executes(c -> execute(c, StringArgumentType.getString(c, "kit"))));
    }

    private static int execute(CommandContext<CommandSource> c, String name) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        if (!Main.kitMap.containsKey(name)) {
            TextUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Kit kit = Main.kitMap.get(name);
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            TextUtils.err(player, Errs.empty_hand());
            return 0;
        }
        if (kit.addItem(stack)) {
            TextUtils.msg(c, Msgs.kit_item_added.get(stack.getDisplayName().getString(), name));
            return 1;
        }
        TextUtils.err(player, Errs.kit_full(name));
        return 0;
    }
}

