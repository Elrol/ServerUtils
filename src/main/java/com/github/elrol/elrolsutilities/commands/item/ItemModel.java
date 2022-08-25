package com.github.elrol.elrolsutilities.commands.item;

import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;

public class ItemModel {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("model")
                    .executes(ItemModel::info)
                        .then(Commands.literal("set")
                                .then(Commands.argument("id", IntegerArgumentType.integer(-16777216,16777216))
                                .executes(ItemModel::execute)))
                        .then(Commands.literal("clear").executes(ItemModel::clear));
    }

    private static int info(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);

        if(stack.isEmpty()) {
            TextUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        CompoundNBT tag = stack.getOrCreateTag();
        if(tag.get("CustomModelData") != null) {
            int id = tag.getInt("CustomModelData");
            TextUtils.msg(player, Msgs.itemModelInfo.get(stack.getHoverName().getString(), String.valueOf(id)));
        } else {
            TextUtils.err(player, Errs.noModelData.get(stack.getHoverName().getString()));
        }
        return 1;
    }

    private static int clear(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);

        if(stack.isEmpty()) {
            TextUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        CompoundNBT tag = stack.getOrCreateTag();
        tag.remove("CustomModelData");
        TextUtils.msg(player, Msgs.clearItemModel.get(stack.getHoverName().getString()));
        return 1;
    }

    private static int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        int id = IntegerArgumentType.getInteger(c, "id");
        ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);

        if(stack.isEmpty()) {
            TextUtils.err(player, Errs.heldItemMissing.get());
            return 0;
        }
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("CustomModelData", id);
        TextUtils.msg(player, Msgs.setItemModel.get(stack.getHoverName().getString(), String.valueOf(id)));
        return 1;
    }
}
