package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class MotdCmd
extends _CmdBase {
    public MotdCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
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

    protected int execute(CommandContext<CommandSource> c, String motd) {
        Main.serverData.setMOTD(motd);
        TextUtils.msg(c.getSource(), Msgs.setMOTD());
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        String motd = Main.serverData.getMotd();
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            c.getSource().sendFailure(new StringTextComponent(TextUtils.formatString(motd)));
            return 1;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        PlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, motd), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity player;
        String motd;

        public CommandRunnable(ServerPlayerEntity player, String motd) {
            this.player = player;
            this.motd = motd;
        }

        @Override
        public void run() {
            player.sendMessage(new StringTextComponent(motd), player.getUUID());
        }
    }

}

