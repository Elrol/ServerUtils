package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.commands.vote.VoteRedeem;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

public class VoteCmd
extends _CmdBase {
    public VoteCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
            dispatcher.register(Commands.literal(a)
                    .executes(this::execute)
                    .then(VoteRedeem.register()));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        if (Main.serverData == null) {
            Logger.err("Server Data was null");
            return 0;
        }
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(c.getSource()), false);
        return 1;
    }

    private static class CommandRunnable implements Runnable {
        CommandSourceStack source;

        public CommandRunnable(CommandSourceStack source) {
            this.source = source;
        }

        @Override
        public void run() {
            MutableComponent text = (MutableComponent) Msgs.voteLinks.get();
            text.append(ChatFormatting.GREEN + "\n─────────────────────────────────");
            Main.vote.getLinks().forEach((site,link) -> {
                text.append(TextUtils.formatString("\n" + site + ": \n    "));
                text.append(ForgeHooks.newChatWithLinks(link));
            });
            text.append(ChatFormatting.GREEN + "\n─────────────────────────────────");
            TextUtils.msg(this.source, text);
        }
    }

}

