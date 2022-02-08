package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.commands.kit.*;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import java.util.List;

public class KitEditCmd
extends _CmdBase {
    public KitEditCmd(int delay, int cooldown, List<String> aliases, int cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(
                        Commands.literal(a)
                                .executes(this::execute)
                                .then(KitCreate.register())
                                .then(KitDelete.register())
                                .then(KitAdd.register())
                                .then(KitRemove.register())
                                .then(KitPermission.register())
                                .then(KitCooldown.register())
                                .then(KitInfo.register())
                                .then(KitFirstJoin.register())
                                .then(KitCost.register())
                                .then(KitClear.register()));
        }
    }

    protected int execute(CommandContext<CommandSource> c){
        String[] help = new String[]{
                "Create:Makes a new kit",
                "Delete:Removes a kit",
                "Add:Puts the current held item int to the kit",
                "Remove:Takes the item stack from the kit",
                "Permission:Displays the permission to get the kit",
                "Cooldown:Sets or displays the cool down for the kit",
                "Info:Displays info about the kit",
                "FirstJoin:Sets a kit as the first join kit",
                "Cost:Sets the cost of the kit",
                "Clear:Removes all items from the kit"
        };
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }
}

