package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.commands.kit.KitClaim;
import com.github.elrol.elrolsutilities.commands.kit.KitInfo;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class KitCmd
extends _CmdBase {
    public KitCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        new KitEditCmd(delay, cooldown, aliases, cost).register(dispatcher);
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register(
                        Commands.literal(a)
                                .executes(this::execute)
                                .then(KitClaim.register())
                                .then(KitInfo.register()));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c){
        String[] help = new String[]{
                "Claim: Claims the kit specified and adding the items to your inventory.",
                "Info: Displays info about a specified kit."
        };
        TextUtils.msgNoTag(c.getSource(), TextUtils.commandHelp(help));
        return 1;
    }
}

