package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class StaffChatCmd
extends _CmdBase {
    public StaffChatCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .executes(this::execute)
                            .then(Commands.argument("msg", StringArgumentType.greedyString())
                                    .executes(c -> execute(c, StringArgumentType.getString(c, "msg")))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, name)) {
            return 0;
        }
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            IPlayerData data = Main.database.get(player.getUUID());
            if (!data.charge(cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        IPlayerData data = Main.database.get(player.getUUID());
        data.toggleStaffChat();
        Main.textUtils.msg(player, Msgs.staff_chat.get(data.usingStaffChat() ? "Enabled" : "Disabled"));
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c, String msg) {
        CommandDelay.init(this, c.getSource(), new CommandRunnable(c.getSource(), msg), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        String msg;

        public CommandRunnable(CommandSourceStack source, String msg) {
            this.source = source;
            this.msg = msg;
        }

        @Override
        public void run() {
            Main.textUtils.sendToStaff(source, msg);
        }
    }

}

