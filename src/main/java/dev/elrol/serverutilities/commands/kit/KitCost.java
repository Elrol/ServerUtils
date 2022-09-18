package dev.elrol.serverutilities.commands.kit;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.data.Kit;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class KitCost {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("cost")
                .then(Commands.argument("kit", StringArgumentType.string())
                        .suggests(ModSuggestions::suggestKits)
                        .requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, CommandConfig.kit_modify_perm.get()))
                        .then(Commands.argument("price", IntegerArgumentType.integer(0))
                            .executes(KitCost::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        String name = StringArgumentType.getString(c, "kit");
        int cost = IntegerArgumentType.getInteger(c,"price");
        if (!Main.kitMap.containsKey(name)) {
            Main.textUtils.err(c, Errs.kit_doesnt_exist(name));
            return 0;
        }
        Kit kit = Main.kitMap.get(name);
        kit.setCost(cost);
        Main.textUtils.msg(c, Msgs.set_cost.get(kit.name, Main.textUtils.parseCurrency(cost, true)));
        return 1;
    }
}
