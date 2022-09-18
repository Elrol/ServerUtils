package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class GodCmd
extends _CmdBase {
    public GodCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : this.aliases) {
            if (this.name.isEmpty()) {
                this.name = a;
            }
            Logger.debug("Registering Alias \"" + a + "\" for Command \"" + this.name + "\"");
            dispatcher.register((Commands.literal(a)
            		.executes(this::execute))
            		.then(Commands.argument("player", EntityArgument.player())
            				.executes(c -> this.execute(c, EntityArgument.getPlayer(c, "player")))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, ServerPlayer target) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(c.getSource(), target), false);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        return this.execute(c, player);
    }

    private static class CommandRunnable
    implements Runnable {
        CommandSourceStack source;
        ServerPlayer player;

        public CommandRunnable(CommandSourceStack source, ServerPlayer player) {
            this.source = source;
            this.player = player;
        }

        @Override
        public void run() {
            boolean flag = !this.player.getAbilities().invulnerable;
            if (this.source.getTextName().equalsIgnoreCase(this.player.getName().getString())) {
                Main.textUtils.msg(this.source, Msgs.god_self.get(flag ? "enabled" : "disabled"));
            } else {
                Main.textUtils.msg(this.source, Msgs.god_other.get(flag ? "enabled" : "disabled", Methods.getDisplayName(this.player)));
                Main.textUtils.msg(this.player, Msgs.god.get(flag ? "enabled" : "disabled"));
            }
            this.player.getAbilities().invulnerable = flag;
            this.player.onUpdateAbilities();
        }
    }

}

