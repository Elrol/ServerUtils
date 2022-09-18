package dev.elrol.serverutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class _CommandTemplate extends _CmdBase {

    public _CommandTemplate(ForgeConfigSpec.ConfigValue<String> perm, ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    public void register(CommandDispatcher<CommandSourceStack> var1) {}

    protected int execute(CommandContext<CommandSourceStack> var1) { return 0; }
    /**
    public _CommandTemplate() {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : this.aliases) {
            PermInit.add(a, this.perm.get());
            if (this.name.isEmpty()) {
                this.name = a;
            }
            Logger.debug("Registering Alias \"" + a + "\" for Command \"" + this.name + "\"");
            dispatcher.register((Commands.literal(a).requires(source -> IElrolAPI.getInstance().getPermissionHandler().hasPermission(source, this.perm))).executes(c -> this.execute(c)));
        }
    }

    @Override
    protected int execute(CommandContext<CommandSourceStack> c) {
        ServerPlayer player = null;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException commandSyntaxException) {
            // empty catch block
        }
        CommandDelay.init(this, player, new CommandRunnable(), false);
        return 1;
    }

    private class CommandRunnable
    implements Runnable {
        @Override
        public void run() {
        }
    }
    **/
}

