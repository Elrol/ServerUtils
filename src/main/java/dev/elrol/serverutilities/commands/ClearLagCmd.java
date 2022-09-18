package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
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

public class ClearLagCmd extends _CmdBase {
    public ClearLagCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : this.aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(this::wipe)
                        .then(Commands.argument("args", StringArgumentType.greedyString())
                                .suggests(ModSuggestions::suggestClearlagTypes)
                                .executes(this::execute))
                ));
        }
    }

    protected int wipe(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }

        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(
                FeatureConfig.clearlag_hostile.get(),
                FeatureConfig.clearlag_passive.get(),
                FeatureConfig.clearlag_items.get()
        ), false);
        return 1;
    }

    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }

        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && this.cost > 0) {
            if (!data.charge(this.cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        String args = StringArgumentType.getString(c, "args");
        if(args == null) args = "";
        if(args.equals("") || (args.contains("hostile") && args.contains("passive") && args.contains("item"))){
            return wipe(c);
        }
        CommandDelay.init(this, c.getSource(), new CommandRunnable(
                args.contains("hostile"),
                args.contains("passive"),
                args.contains("item")
        ), false);
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        boolean hostile;
        boolean passive;
        boolean items;

        public CommandRunnable(boolean h, boolean p, boolean i){
            hostile = h;
            passive = p;
            items = i;
        }

        @Override
        public void run() {
            Methods.clearlag(hostile, passive, items);
        }
    }

}

