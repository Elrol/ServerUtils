package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.ServerData;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class UnjailCmd
extends _CmdBase {

    public UnjailCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("player", EntityArgument.players())
                                .executes(c -> unjail(c, EntityArgument.getPlayer(c,"player")))
                        )
                );
        }
    }

    protected int unjail(CommandContext<CommandSource> c, ServerPlayerEntity player) {
        ServerData serverData = Main.serverData;
        if(player == null) {
            TextUtils.err(c.getSource(), Errs.player_missing());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if(!data.isJailed()) {
            TextUtils.err(c.getSource(), Errs.not_jailed(data.getDisplayName()));
            return 0;
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(c.getSource(), player), false);
        return 1;
    }

    protected int create(CommandContext<CommandSource> c, String name) {
        if(Main.serverData.getJail(name) != null) {
            TextUtils.err(c.getSource(), Errs.jail_exists(name));
            return 0;
        }
        Main.serverData.createJail(name);
        TextUtils.msg(c.getSource(), Msgs.jail_created.get(name));
        return 1;
    }

    protected int delete(CommandContext<CommandSource> c, String jail) {
        if(Main.serverData.getJail(jail) == null) {
            TextUtils.err(c, Errs.jail_missing());
        }
        Main.serverData.deleteJail(jail);
        TextUtils.msg(c.getSource(), Msgs.jail_deleted.get(name));
        return 1;
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        String[] help = new String[]{
                "Unjail [player]: Releases the player from the jail they are in, if any."
        };
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }

    private static class CommandRunnable implements Runnable {
        CommandSource source;
        ServerPlayerEntity player;

        public CommandRunnable(CommandSource source, ServerPlayerEntity player) {
            this.source = source;
            this.player = player;
        }

        @Override
        public void run() {
            IPlayerData data = Main.database.get(player.getUUID());
            Main.serverData.unjail(player);
            TextUtils.msg(source, Msgs.unjailed_player.get(data.getDisplayName()));
            TextUtils.msg(player, Msgs.unjailed.get());
        }
    }
}

