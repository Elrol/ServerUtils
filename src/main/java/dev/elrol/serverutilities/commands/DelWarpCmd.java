package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
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

public class DelWarpCmd
extends _CmdBase {
    public DelWarpCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(Commands.literal(a)
                        .then(Commands.argument("name", StringArgumentType.string())
                                .suggests(ModSuggestions::suggestWarps)
                                .executes(this::execute)));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        String name = StringArgumentType.getString(c, "name");
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        if (name.isEmpty()) {
            Main.textUtils.err(c, Errs.no_warp_name());
            return 0;
        }
        if (Main.serverData.getWarp(name) == null) {
            Main.textUtils.err(c, Errs.warp_not_found(name));
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(c.getSource(), name), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        String name;

        public CommandRunnable(CommandSourceStack source, String name) {
            this.source = source;
            this.name = name;
        }

        @Override
        public void run() {
            Main.serverData.delWarp(this.name);
            Main.textUtils.msg(this.source, Msgs.delWarp.get(this.name));
        }
    }

}

