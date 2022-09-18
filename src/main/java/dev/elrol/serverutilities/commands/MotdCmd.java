package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Logger;
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
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class MotdCmd extends _CmdBase {
    public MotdCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .executes(this::execute)
                        .then(Commands.literal("set")
                                .then(Commands.argument("motd", StringArgumentType.greedyString())
                                        .requires(p -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(p, CommandConfig.motd_modify_perm.get()))
                                        .executes(c -> execute(c, StringArgumentType.getString(c, "motd"))))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, String motd) {
        Main.serverData.setMOTD(motd);
        Main.textUtils.msg(c.getSource(), Msgs.setMOTD.get());
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        String motd = Main.serverData.getMotd();
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            c.getSource().sendFailure(Component.literal(Main.textUtils.formatString(motd)));
            return 1;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, motd), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer player;
        String motd;

        public CommandRunnable(ServerPlayer player, String motd) {
            this.player = player;
            this.motd = motd;
        }

        @Override
        public void run() {
            player.sendSystemMessage(Component.literal(motd));
        }
    }

}

