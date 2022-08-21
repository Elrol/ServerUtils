package com.github.elrol.elrolsutilities.commands.item;

import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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

public class ItemModel {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("model")
                    .executes(ItemModel::info)
                        .then(Commands.literal("set")
                                .then(Commands.argument("id", IntegerArgumentType.integer(-16777216,16777216))
                                .executes(ItemModel::execute)))
                        .then(Commands.literal("clear").executes(ItemModel::clear));
    }

    private static int info(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(stack.isEmpty()) {
            TextUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        if(tag.get("CustomModelData") != null) {
            int id = tag.getInt("CustomModelData");
            TextUtils.msg(player, Msgs.itemModelInfo.get(stack.getHoverName().getString(), String.valueOf(id)));
        } else {
            player.sendSystemMessage(Component.literal(stack.getHoverName().getString()).append(" has no CustomModelData"));
        }
        return 1;
    }

    private static int clear(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(stack.isEmpty()) {
            TextUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove("CustomModelData");
        player.sendSystemMessage(Component.literal("Removed " + stack.getHoverName().getString()).append("'s CustomModelData"));
        return 1;
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
        int id = IntegerArgumentType.getInteger(c, "id");
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(stack.isEmpty()) {
            TextUtils.err(player, Errs.heldItemMissing.get());
            return 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        if(tag != null) {
            tag.putInt("CustomModelData", id);
            player.sendSystemMessage(Component.literal("Set " + stack.getHoverName().getString()).append("'s CustomModelData to: ").append(String.valueOf(id)));
        } else {
            Logger.err("Tag was empty");
        }
        return 1;
    }
}
