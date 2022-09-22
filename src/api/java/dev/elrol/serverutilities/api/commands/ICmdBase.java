package dev.elrol.serverutilities.api.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public interface ICmdBase {
    void register(CommandDispatcher<CommandSourceStack> var1);
}
