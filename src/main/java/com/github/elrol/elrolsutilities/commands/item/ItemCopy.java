package com.github.elrol.elrolsutilities.commands.item;

import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ItemCopy {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("copy")
                .executes(ItemCopy::execute);
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
        ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);

        if(stack.isEmpty()) {
            TextUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        CompoundNBT tag = stack.getOrCreateTag();

        ResourceLocation item = stack.getItem().getRegistryName();
        assert item != null;
        StringSelection select = new StringSelection(item.toString() + tag);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(select, null);
        TextUtils.msg(player, Msgs.copiedItem.get(stack.getDisplayName().getString()));
        return 1;
    }
}
