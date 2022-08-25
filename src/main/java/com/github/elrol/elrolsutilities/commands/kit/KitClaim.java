package com.github.elrol.elrolsutilities.commands.kit;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.commands.ModSuggestions;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.data.Kit;
import com.github.elrol.elrolsutilities.init.CommandRegistry;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class KitClaim {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("claim")
        		.then(Commands.argument("kit", StringArgumentType.string())
        				.suggests(ModSuggestions::suggestKits)
                        .executes(c -> execute(c, StringArgumentType.getString(c, "kit"))));
    }

    protected static int execute(CommandContext<CommandSource> c, String kitname) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        if (!Main.kitMap.containsKey(kitname)) {
            TextUtils.err(player, Errs.kit_doesnt_exist(kitname));
            return 0;
        }

        Kit kit = Main.kitMap.get(kitname);
        if (!IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.getUUID(), kit.getPerm().get())) {
            TextUtils.err(c, Errs.no_permission());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        Long timeLeft = data.tillUseKit(kit);
        if (timeLeft > 0L) {
            TextUtils.err(c, Msgs.kit_in_cd.get(kit.name, timeLeft + (timeLeft > 1L ? " minutes" : " minute")));
            return 0;
        }
        if(timeLeft < 0L) {
            TextUtils.err(player, Errs.kit_claimed(kit.name));
            return 0;
        }
        int cost = CommandConfig.kit.cost.get();
        if (FeatureConfig.enable_economy.get()) {
            if(kit.getCost() > 0){
                if(!data.charge(kit.getCost())){
                    TextUtils.err(player, Errs.not_enough_funds(kit.getCost(), data.getBal()));
                    return 0;
                }
            } else if(cost > 0){
                if(!data.charge(cost)){
                    TextUtils.err(player, Errs.not_enough_funds(cost, data.getBal()));
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
        ServerPlayerEntity player;
        IPlayerData data;

        public CommandRunnable(ServerPlayerEntity player, IPlayerData data, Kit kit) {
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
            TextUtils.msg(player, Msgs.received_kit.get(kit.name));
            Main.getLogger().info(data.getDisplayName() + " claimed Kit " + kit.name + (kit.getCost() > 0 ? " for " + TextUtils.parseCurrency(kit.getCost(), true) : ""));
        }
    }
}

