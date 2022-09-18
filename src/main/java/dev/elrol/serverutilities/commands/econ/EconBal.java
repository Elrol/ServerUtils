package dev.elrol.serverutilities.commands.econ;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class EconBal {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("bal")
                .then(Commands.argument("target", EntityArgument.player())
                        .executes(EconBal::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        try {
            ServerPlayer player = EntityArgument.getPlayer(c, "target");
            IPlayerData data = Main.database.get(player.getUUID());
            String cur = Main.textUtils.parseCurrency(data.getBal(), false);

            if(player.getName().equals(c.getSource().getDisplayName())) {
                Main.textUtils.msg(c, Msgs.bal_self.get(cur));
            } else {
                Main.textUtils.msg(c, Msgs.bal_other.get(data.getDisplayName(), cur));
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
