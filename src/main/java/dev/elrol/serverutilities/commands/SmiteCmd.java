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
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class SmiteCmd
        extends _CmdBase {
    public SmiteCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(c -> execute(c, EntityArgument.getPlayer(c, "player")))));
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
        return 0;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException commandSyntaxException) {
            // empty catch block
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
            BlockPos pos = player.blockPosition();
            ServerLevel world = (ServerLevel) player.level;
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
            bolt.moveTo(Vec3.atCenterOf(pos));
            world.addFreshEntity(bolt);
            if (source.getTextName().equalsIgnoreCase(player.getName().getString())) {
                Main.textUtils.err(source, Errs.smitten_self());
            } else {
                Main.textUtils.msg(source, Msgs.smite.get(Methods.getDisplayName(player)));
                Main.textUtils.msg(player, Msgs.smitten.get());
            }
        }
    }

}

