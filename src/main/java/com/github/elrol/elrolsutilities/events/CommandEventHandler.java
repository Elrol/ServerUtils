package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandEventHandler {
    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        ParseResults<CommandSource> pr = event.getParseResults();
        if(FeatureConfig.enable_global_perms.get().equals(false)) return;
        CommandSource source = pr.getContext().getSource();
        String perm = "";
        String node = null;
        for (ParsedCommandNode<CommandSource> a : pr.getContext().getNodes()) {
            node = (node == null ? a.getNode().getName() : node + "_" + a.getNode().getName());
            if (Main.permRegistry.commandPerms.containsKey(node)) {
                perm = Main.permRegistry.commandPerms.get(node);
            } else {
                perm = "command." + node.replace("_", ".");
                Main.permRegistry.add(node, perm);
                Logger.log("Permission[" + perm + "] Created for Command[" + node + "] By: " + source.getTextName());
            }
            Logger.debug("Command to string: " + a.getNode().getName());
        }
        try {
            ServerPlayerEntity player = source.getPlayerOrException();
            PlayerData data = Main.database.get(player.getUUID());
            if (data.custperm != null && data.custperm.equals("")) {
                data.custperm = node;
                if (node == null) {
                    TextUtils.err(player, Errs.invalid_cmd());
                } else {
                    TextUtils.msg(player, Msgs.custperm_1(node));
                }
                event.setCanceled(true);
                return;
            }
        }
        catch (CommandSyntaxException data) {
            // empty catch block
        }
        Logger.debug(node + " : " + perm);
        if (IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, perm)) {
            Logger.debug("Permission Granted");
            return;
        }
        if (perm.isEmpty() && !FeatureConfig.enable_global_perms.get()) {
            Logger.debug("Permissions not Enabled");
            return;
        }
        if (!IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, perm)) {
            TextUtils.err(source, Errs.no_permission());
            event.setCanceled(true);
            return;
        }
        Logger.debug("This should only run if you do have permission");
    }
}

