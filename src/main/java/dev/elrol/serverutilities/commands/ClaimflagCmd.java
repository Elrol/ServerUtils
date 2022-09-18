package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.enums.ClaimFlagKeys;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ClaimflagCmd extends _CmdBase {
    public ClaimflagCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : this.aliases) {
            if(name.isEmpty()) name = a;
            dispatcher.register(Commands.literal(a)
                    .executes(this::execute)
                    .then(Commands.argument("flag", StringArgumentType.string())
                            .suggests(ModSuggestions::suggestFlags)
                            .executes(this::check)
                            .then(Commands.argument("value", BoolArgumentType.bool())
                                    .executes(this::set)
                            )
                    )
            );
        }
    }

    private int set(CommandContext<CommandSourceStack> c){
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        String s = StringArgumentType.getString(c, "flag");
        if(ClaimFlagKeys.contains(s)){
            boolean bool = BoolArgumentType.getBool(c, "value");
            IPlayerData data = Main.database.get(player.getUUID());
            if(FeatureConfig.enable_economy.get() && this.cost > 0){
                if(!data.charge(this.cost)){
                    Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return 0;
                }
            }
            CommandDelay.init(this, player, new CommandRunnable(player, ClaimFlagKeys.valueOf(s), bool), false);
            return 1;
        } else {
            Main.textUtils.err(player, Errs.no_flag(s));
            return 0;
        }
    }

    private int check(CommandContext<CommandSourceStack> c){
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        String s = StringArgumentType.getString(c, "flag");
        if(ClaimFlagKeys.contains(s)){
            IPlayerData data = Main.database.get(player.getUUID());
            boolean flag = data.getClaimFlags().get(ClaimFlagKeys.valueOf(s));
            Main.textUtils.msg(player, Msgs.claim_flag_check.get(s, String.valueOf(flag)));
            return 1;
        } else {
            Main.textUtils.err(player, Errs.no_flag(s));
            return 0;
        }
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
        Main.textUtils.msg(player, Msgs.claim_flags.get(ClaimFlagKeys.list().toString()));
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer player;
        ClaimFlagKeys flag;
        boolean value;

        public CommandRunnable(ServerPlayer target, ClaimFlagKeys flag, boolean value) {
            this.player = target;
            this.flag = flag;
            this.value = value;
        }

        @Override
        public void run() {
            IPlayerData data = Main.database.get(player.getUUID());
            data.setFlag(flag, value);
            Main.textUtils.msg(player, Msgs.set_claim_flag.get(flag.name(), String.valueOf(value)));
        }
    }

}

