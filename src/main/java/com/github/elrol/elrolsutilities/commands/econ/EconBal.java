package com.github.elrol.elrolsutilities.commands.econ;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class EconBal {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("bal")
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(EconBal::execute));
    }

    private static int execute(CommandContext<CommandSource> c) {
        try {
            ServerPlayerEntity player = EntityArgument.getPlayer(c, "target");
            IPlayerData data = Main.database.get(player.getUUID());
            String cur = TextUtils.parseCurrency(data.getBal(), false);

            if(player.getName().equals(c.getSource().getDisplayName())) {
                TextUtils.msg(c, Msgs.bal_self.get(cur));
            } else {
                TextUtils.msg(c, Msgs.bal_other.get(data.getDisplayName(), cur));
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
