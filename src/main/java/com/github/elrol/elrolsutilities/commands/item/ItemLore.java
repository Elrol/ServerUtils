package com.github.elrol.elrolsutilities.commands.item;

import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Hand;

public class ItemLore {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("lore")
                    .then(Commands.literal("add")
                        .then(Commands.argument("text", StringArgumentType.greedyString())
                                .executes(ItemLore::add)))
                    .then(Commands.literal("set")
                        .then(Commands.argument("line", IntegerArgumentType.integer(0))
                            .then(Commands.argument("text", StringArgumentType.greedyString())
                                .executes(ItemLore::set))))
                    .then(Commands.literal("remove")
                        .then(Commands.argument("line", IntegerArgumentType.integer(0))
                                .executes(ItemLore::remove)))
                    .then(Commands.literal("clear")
                        .executes(ItemLore::clear));
    }

    private static int add(CommandContext<CommandSource> c) {
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
            return 0;
        }
        String text = TextUtils.formatString(StringArgumentType.getString(c, "text"));
        ListNBT loreList = getLore(stack);
        loreList.add(StringNBT.valueOf("[{\"text\":\"" + text + "\"}]"));

        TextUtils.msg(player, Msgs.itemLoreAdd.get(text, stack.getHoverName().getString()));
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
            return 0;
        }
        ListNBT loreList = getLore(stack);
        loreList.clear();

        TextUtils.msg(player, Msgs.itemLoreClear.get(stack.getHoverName().getString()));
        return 1;
    }

    private static int remove(CommandContext<CommandSource> c) {
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
            return 0;
        }
        int line = IntegerArgumentType.getInteger(c, "line") - 1;
        ListNBT loreList = getLore(stack);
        loreList.remove(line);

        TextUtils.msg(player, Msgs.itemLoreRemove.get(String.valueOf(line + 1), stack.getHoverName().getString()));
        return 1;
    }

    private static int set(CommandContext<CommandSource> c) {
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
            return 0;
        }
        int line = IntegerArgumentType.getInteger(c, "line") - 1;
        String text = TextUtils.formatString(StringArgumentType.getString(c, "text"));
        ListNBT loreList = getLore(stack);
        loreList.set(line, StringNBT.valueOf("[{\"text\":\"" + text + "\"}]"));

        TextUtils.msg(player, Msgs.itemLoreSet.get(String.valueOf(line + 1), stack.getHoverName().getString(), text));
        return 1;
    }

    private static ListNBT getLore(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        CompoundNBT display = tag.getCompound("display");
        ListNBT lore = display.getList("Lore", 8);
        display.put("Lore", lore);
        tag.put("display", display);
        return lore;
    }
}
