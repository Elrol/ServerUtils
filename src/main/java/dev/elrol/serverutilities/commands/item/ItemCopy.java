package dev.elrol.serverutilities.commands.item;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ItemCopy {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("copy")
                .executes(ItemCopy::execute);
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
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(stack.isEmpty()) {
            Main.textUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        ResourceLocation item = ForgeRegistries.ITEMS.getKey(stack.getItem());
        assert item != null;
        String output = item.toString();
        if(stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if(tag != null) output += tag.toString();
        }
        try {
            StringSelection select = new StringSelection(output);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(select, null);
        } catch (Exception e) {
            Logger.log("Item Data: " + output);
        }
        Main.textUtils.msg(player, Msgs.copiedItem.get(stack.getHoverName().getString()));
        return 1;
    }
}
