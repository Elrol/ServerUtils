package dev.elrol.serverutilities.commands.econ;

import com.mojang.brigadier.arguments.IntegerArgumentType;
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

public class EconRemove {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("remove")
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                        .executes(EconRemove::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> c) {
        try {
            ServerPlayer player = EntityArgument.getPlayer(c, "target");
            int amount = IntegerArgumentType.getInteger(c,"amount");
            IPlayerData data = Main.database.get(player.getUUID());
            data.charge(amount);
            String cur = Main.textUtils.parseCurrency(amount, false);

            if(player.getName().equals(c.getSource().getDisplayName())) {
                Main.textUtils.msg(c, Msgs.charged_self.get(cur));
            } else {
                Main.textUtils.msg(c, Msgs.charged_player.get(data.getDisplayName(), cur));
                Main.textUtils.msg(player, Msgs.charged_by.get("The Server", cur));
            }
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
