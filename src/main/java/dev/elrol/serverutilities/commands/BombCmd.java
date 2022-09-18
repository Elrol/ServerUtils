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
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BombCmd extends _CmdBase {
    public BombCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
            dispatcher.register((Commands.literal(a)
                    .executes(this::execute))
                    .then(Commands.argument("players", EntityArgument.players())
                            .executes(c -> this.execute(c, EntityArgument.getPlayers(c, "players")))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, Collection<ServerPlayer> players) {
        players.forEach(player -> {
            IPlayerData data = Main.database.get(player.getUUID());
            if(FeatureConfig.enable_economy.get() && this.cost > 0){
                if(!data.charge(this.cost)){
                    Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return;
                }
            }
            CommandDelay.init(this, player, new CommandRunnable(c.getSource(), player), false);
        });
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if(FeatureConfig.enable_economy.get() && this.cost > 0){
            if(!data.charge(this.cost)){
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        return execute(c, Collections.singleton(player));
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer player;
        CommandSourceStack source;

        public CommandRunnable(CommandSourceStack source, ServerPlayer target) {
            this.player = target;
            this.source = source;
        }

        @Override
        public void run() {
            BlockPos pos = new BlockPos(player.blockPosition());
            ServerLevel level = player.getLevel();

            PrimedTnt tnt = new PrimedTnt(level, pos.getX(), pos.getY(), pos.getZ(), player);
            tnt.setFuse(10);
            level.addFreshEntity(tnt);
            Logger.debug(Methods.getDisplayName(player));
            if (source.getTextName().equalsIgnoreCase(player.getName().getString())) {
                Main.textUtils.err(player, Errs.bombed_self());
            } else {
                Main.textUtils.msg(source, Msgs.bombed.get(Methods.getDisplayName(player)));
                Main.textUtils.msg(player, Msgs.boom.get());
            }
        }
    }

}

