package dev.elrol.serverutilities.events;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.text.Errs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandEventHandler {
    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        ParseResults<CommandSourceStack> pr = event.getParseResults();
        CommandContextBuilder<CommandSourceStack> context = pr.getContext();
        CommandSourceStack source = context.getSource();

        StringBuilder cmd = new StringBuilder(context.getRootNode().getName());
        for (ParsedCommandNode<CommandSourceStack> node : context.getNodes()) {
            cmd.append(" ").append(node.getNode().getName());
        }

        String dimLoc = source.getLevel().dimension().location().toString();
        Main.blacklists.dimension_command_blacklist.get(dimLoc).forEach(c->{
            if(cmd.toString().startsWith(c)) event.setCanceled(true);
        });

        if(event.isCanceled()) Main.textUtils.err(source, Errs.cant_use_cmd_here.get());

        /**
        String perm = "";
        String node = null;
        for (ParsedCommandNode<CommandSourceStack> a : pr.getContext().getNodes()) {
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
            Main.textUtils.err(source, Errs.no_permission());
            event.setCanceled(true);
            return;
        }
        Logger.debug("This should only run if you do have permission");
        **/
    }
}

