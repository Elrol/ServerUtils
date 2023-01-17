package dev.elrol.serverutilities.commands.kit;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.commands.ModSuggestions;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.data.Kit;
import dev.elrol.serverutilities.init.CommandRegistry;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class KitClaim {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("claim")
        		.then(Commands.argument("kit", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .executes(c -> execute(c, StringArgumentType.getString(c, "kit"))));
    }

    protected static int execute(CommandContext<CommandSourceStack> c, String kitname) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        if (!Main.kitMap.containsKey(kitname)) {
            Main.textUtils.err(player, Errs.kit_doesnt_exist(kitname));
            return 0;
        }

        Kit kit = Main.kitMap.get(kitname);
        if (!IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.getUUID(), kit.getPerm().get())) {
            Main.textUtils.err(c, Errs.no_permission());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        Long timeLeft = data.tillUseKit(kit);
        if (timeLeft > 0L) {
            Main.textUtils.err(c, Msgs.kit_in_cd.get(kit.name, timeLeft + (timeLeft > 1L ? " minutes" : " minute")));
            return 0;
        }
        if(timeLeft < 0L) {
            Main.textUtils.err(player, Errs.kit_claimed(kit.name));
            return 0;
        }
        int cost = CommandConfig.kit.cost.get();
        if (FeatureConfig.enable_economy.get()) {
            if(kit.getCost() > 0){
                if(!data.charge(kit.getCost())){
                    Main.textUtils.err(player, Errs.not_enough_funds(kit.getCost(), data.getBal()));
                    return 0;
                }
            } else if(cost > 0){
                if(!data.charge(cost)){
                    Main.textUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
                    return 0;
                }
            }
        }
        CommandDelay.init(CommandRegistry.kitCmd, player, new CommandRunnable(player, data, kit), false);
        return 1;
    }


    private static class CommandRunnable
            implements Runnable {
        Kit kit;
        ServerPlayer player;
        IPlayerData data;

        public CommandRunnable(ServerPlayer player, IPlayerData data, Kit kit) {
            this.kit = kit;
            this.player = player;
            this.data = data;
        }

        @Override
        public void run() {
            kit.give(player);
            int timeNow = Methods.tickToMin(Main.mcServer.getNextTickTime());
            Logger.debug("adding kit cooldown to data " + timeNow);
            if (kit.cooldown > 0) {
                data.getKitCooldowns().put(kit.name, timeNow);
            }
            Main.textUtils.msg(player, Msgs.received_kit.get(kit.name));
            Main.getLogger().info(data.getDisplayName() + " claimed Kit " + kit.name + (kit.getCost() > 0 ? " for " + Main.textUtils.parseCurrency(kit.getCost(), true) : ""));
        }
    }
}

