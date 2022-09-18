package dev.elrol.serverutilities.commands.item;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
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
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(stack.isEmpty()) {
            Main.textUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        if(tag.get("CustomModelData") != null) {
            int id = tag.getInt("CustomModelData");
            Main.textUtils.msg(player, Msgs.itemModelInfo.get(stack.getHoverName().getString(), String.valueOf(id)));
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
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(stack.isEmpty()) {
            Main.textUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove("CustomModelData");
        Main.textUtils.msg(player, Msgs.clearItemModel.get(stack.getHoverName().getString()));
        return 1;
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        int id = IntegerArgumentType.getInteger(c, "id");
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(stack.isEmpty()) {
            Main.textUtils.err(player, Errs.heldItemMissing.get());
            return 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("CustomModelData", id);
        Main.textUtils.msg(player, Msgs.setItemModel.get(stack.getHoverName().getString(), String.valueOf(id)));
        return 1;
    }
}
