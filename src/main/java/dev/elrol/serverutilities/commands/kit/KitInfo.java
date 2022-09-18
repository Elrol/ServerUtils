package dev.elrol.serverutilities.commands.kit;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.data.Kit;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
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
            Main.textUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Kit kit = Main.kitMap.get(name);
        List<ItemStack> itemList = kit.getKit();
        kit.save();
        Main.kitMap.put(name, kit);
        Main.textUtils.msg(c, Msgs.kit_info.get(name));
        int offset = 3;
        String[] info = new String[itemList.size() + offset];

        info[0] = "Cooldown:" + kit.cooldown + " Minutes";
        info[1] = "Cost:" + Main.textUtils.parseCurrency(kit.getCost(), true);
        info[2] = "Contents";
        for (int i = offset; i < itemList.size() + offset; ++i) {
            ItemStack item = itemList.get(i - offset);
            info[i] = "x" + item.getCount() + ":" + item.getHoverName().getString();
        }

        Main.textUtils.msgNoTag(c.getSource(), Main.textUtils.commandHelp(info));
        return 1;
    }
}

