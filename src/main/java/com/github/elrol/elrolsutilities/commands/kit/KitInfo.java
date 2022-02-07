package com.github.elrol.elrolsutilities.commands.kit;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.data.Kit;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class KitInfo {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("info")
        		.then(Commands.argument("name", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
        				.executes(c -> KitInfo.execute(c, StringArgumentType.getString(c, "name"))));
    }

    private static int execute(CommandContext<CommandSourceStack> c, String name) {
        if (Main.kitMap == null) {
            Logger.err("KitMap is null.");
            return 0;
        }
        if (!Main.kitMap.containsKey(name)) {
            TextUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Kit kit = Main.kitMap.get(name);
        List<ItemStack> itemList = kit.getKit();
        kit.save();
        Main.kitMap.put(name, kit);
        TextUtils.msg(c, Msgs.kit_info(name));
        int offset = 3;
        String[] info = new String[itemList.size() + offset];

        info[0] = "Cooldown:" + kit.cooldown + " Minutes";
        info[1] = "Cost:" + TextUtils.parseCurrency(kit.getCost(), true);
        info[2] = "Contents";
        for (int i = offset; i < itemList.size() + offset; ++i) {
            ItemStack item = itemList.get(i - offset);
            info[i] = "x" + item.getCount() + ":" + item.getHoverName().getString();
        }

        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(info));
        return 1;
    }
}

