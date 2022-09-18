package dev.elrol.serverutilities.commands.item;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
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
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        String name = StringArgumentType.getString(c, "name");
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if(stack.isEmpty()) {
            Main.textUtils.err(player, Errs.heldItemMissing.get());
            Logger.err("Stack was empty");
            return 0;
        }
        CompoundTag tag = stack.getOrCreateTag();
        stack.setHoverName(Component.literal(Main.textUtils.formatString(name)));
        Main.textUtils.msg(player, Msgs.setItemName.get(stack.getDisplayName().getString()));
        return 1;
    }
}
