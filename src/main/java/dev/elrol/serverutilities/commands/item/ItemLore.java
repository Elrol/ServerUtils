package dev.elrol.serverutilities.commands.item;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ItemLore {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
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

    private static int add(CommandContext<CommandSourceStack> c) {
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
            return 0;
        }
        String text = Main.textUtils.formatString(StringArgumentType.getString(c, "text"));
        ListTag loreList = getLore(stack);
        loreList.add(StringTag.valueOf("[{\"text\":\"" + text + "\"}]"));

        Main.textUtils.msg(player, Msgs.itemLoreAdd.get(text, stack.getHoverName().getString()));
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
            return 0;
        }
        ListTag loreList = getLore(stack);
        loreList.clear();

        Main.textUtils.msg(player, Msgs.itemLoreClear.get(stack.getHoverName().getString()));
        return 1;
    }

    private static int remove(CommandContext<CommandSourceStack> c) {
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
            return 0;
        }
        int line = IntegerArgumentType.getInteger(c, "line") - 1;
        ListTag loreList = getLore(stack);
        loreList.remove(line);

        Main.textUtils.msg(player, Msgs.itemLoreRemove.get(String.valueOf(line + 1), stack.getHoverName().getString()));
        return 1;
    }

    private static int set(CommandContext<CommandSourceStack> c) {
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
            return 0;
        }
        int line = IntegerArgumentType.getInteger(c, "line") - 1;
        String text = Main.textUtils.formatString(StringArgumentType.getString(c, "text"));
        ListTag loreList = getLore(stack);
        loreList.set(line,StringTag.valueOf("[{\"text\":\"" + text + "\"}]"));

        Main.textUtils.msg(player, Msgs.itemLoreSet.get(String.valueOf(line + 1), stack.getHoverName().getString(), text));
        return 1;
    }

    private static ListTag getLore(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag display = tag.getCompound(ItemStack.TAG_DISPLAY);
        ListTag lore = display.getList(ItemStack.TAG_LORE, ListTag.TAG_STRING);
        display.put(ItemStack.TAG_LORE, lore);
        tag.put(ItemStack.TAG_DISPLAY, display);
        return lore;
    }
}
