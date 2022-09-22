package dev.elrol.serverutilities.api.init;

import com.mojang.brigadier.CommandDispatcher;
import dev.elrol.serverutilities.api.commands.ICmdBase;
import net.minecraft.commands.CommandSourceStack;

public interface ICommandRegistry {

    void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher);
    void initCommand(ICmdBase cmd);

}
