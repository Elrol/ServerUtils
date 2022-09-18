package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.data.ServerData;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class UnjailCmd
extends _CmdBase {

    public UnjailCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::execute))
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(c -> unjail(c, EntityArgument.getPlayer(c, "player")))
                        )
                );
        }
    }

    protected int unjail(CommandContext<CommandSourceStack> c, ServerPlayer player) {
        ServerData serverData = Main.serverData;
        if(player == null) {
            Main.textUtils.err(c.getSource(), Errs.player_missing());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if(!data.isJailed()) {
            Main.textUtils.err(c.getSource(), Errs.not_jailed(data.getDisplayName()));
            return 0;
        }
        CommandDelay.init(this, c.getSource(), new UnjailCommandRunnable(c.getSource(), player), false);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        String[] help = new String[]{
                "Jail [player] [jail] [cell]: Sends the player to the specified cell in the Jail",
                "Jail create [name]: Creates a Jail with the name",
                "Jail delete [jail]: Deletes a Jail with the name",
                "Jail cell add [jail]: Adds a cell at the current location to the Jail",
                "Jail cell remove [jail] [cell]: Removes the cell from the Jail"
        };
        Main.textUtils.msgNoTag(c.getSource(), Main.textUtils.commandHelp(help));
        return 1;
    }

    private static class UnjailCommandRunnable implements Runnable {
        CommandSourceStack source;
        ServerPlayer player;

        public UnjailCommandRunnable(CommandSourceStack source, ServerPlayer player) {
            this.source = source;
            this.player = player;
        }

        @Override
        public void run() {
            IPlayerData data = Main.database.get(player.getUUID());
            Main.serverData.unjail(player);
            Main.textUtils.msg(source, Msgs.unjailed_player.get(data.getDisplayName()));
            Main.textUtils.msg(player, Msgs.unjailed.get());
        }
    }
}

