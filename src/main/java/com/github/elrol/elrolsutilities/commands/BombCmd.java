package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BombCmd extends _CmdBase {
    public BombCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
            dispatcher.register((Commands.literal(a)
                    .executes(this::execute))
                    .then(Commands.argument("players", EntityArgument.players())
                            .executes(c -> this.execute(c, EntityArgument.getPlayers(c, "players")))));
        }
    }

    protected int execute(CommandContext<CommandSource> c, Collection<ServerPlayerEntity> players) {
        players.forEach(player -> {
            IPlayerData data = Main.database.get(player.getUUID());
            if(FeatureConfig.enable_economy.get() && this.cost > 0){
                if(!data.charge(this.cost)){
                    TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return;
                }
            }
            CommandDelay.init(this, player, new CommandRunnable(c.getSource(), player), false);
        });
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if(FeatureConfig.enable_economy.get() && this.cost > 0){
            if(!data.charge(this.cost)){
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        return execute(c, Collections.singleton(player));
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity player;
        CommandSource source;

        public CommandRunnable(CommandSource source, ServerPlayerEntity target) {
            this.player = target;
            this.source = source;
        }

        @Override
        public void run() {
            BlockPos pos = new BlockPos(player.blockPosition());
            ServerWorld level = player.getLevel();

            TNTEntity tnt = new TNTEntity(level, pos.getX(), pos.getY(), pos.getZ(), player);
            tnt.setFuse(10);
            level.addFreshEntity(tnt);
            Logger.debug(Methods.getDisplayName(player));
            if (source.getTextName().equalsIgnoreCase(player.getName().getString())) {
                TextUtils.err(player, Errs.bombed_self());
            } else {
                TextUtils.msg(source, Msgs.bombed.get(Methods.getDisplayName(player)));
                TextUtils.msg(player, Msgs.boom.get());
            }
        }
    }

}

