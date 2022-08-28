package com.github.elrol.elrolsutilities.commands.item;

import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ItemName {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("name")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                        .executes(ItemName::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        String name = StringArgumentType.getString(c, "name");
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(stack.isEmpty()) {
            TextUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        stack.setHoverName(Component.literal(TextUtils.formatString(name)));
        TextUtils.msg(player, Msgs.setItemName.get(stack.getDisplayName().getString()));
        return 1;
    }
}
