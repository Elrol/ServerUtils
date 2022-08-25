package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.api.enums.ClaimFlagKeys;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ClaimflagCmd extends _CmdBase {
    public ClaimflagCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
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

    private int set(CommandContext<CommandSource> c){
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        String s = StringArgumentType.getString(c, "flag");
        if(ClaimFlagKeys.contains(s)){
            boolean bool = BoolArgumentType.getBool(c, "value");
            IPlayerData data = Main.database.get(player.getUUID());
            if(FeatureConfig.enable_economy.get() && this.cost > 0){
                if(!data.charge(this.cost)){
                    TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return 0;
                }
            }
            CommandDelay.init(this, player, new CommandRunnable(player, ClaimFlagKeys.valueOf(s), bool), false);
            return 1;
        } else {
            TextUtils.err(player, Errs.no_flag(s));
            return 0;
        }
    }

    private int check(CommandContext<CommandSource> c){
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        String s = StringArgumentType.getString(c, "flag");
        if(ClaimFlagKeys.contains(s)){
            IPlayerData data = Main.database.get(player.getUUID());
            boolean flag = data.getClaimFlags().get(ClaimFlagKeys.valueOf(s));
            TextUtils.msg(player, Msgs.claim_flag_check.get(s, String.valueOf(flag)));
            return 1;
        } else {
            TextUtils.err(player, Errs.no_flag(s));
            return 0;
        }
    }

    @Override
    protected int execute(CommandContext<CommandSource> c) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        }
        catch (CommandSyntaxException e) {
            TextUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        TextUtils.msg(player, Msgs.claim_flags.get(ClaimFlagKeys.list().toString()));
        return 1;
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity player;
        ClaimFlagKeys flag;
        boolean value;

        public CommandRunnable(ServerPlayerEntity target, ClaimFlagKeys flag, boolean value) {
            this.player = target;
            this.flag = flag;
            this.value = value;
        }

        @Override
        public void run() {
            IPlayerData data = Main.database.get(player.getUUID());
            data.setFlag(flag, value);
            TextUtils.msg(player, Msgs.set_claim_flag.get(flag.name(), String.valueOf(value)));
        }
    }

}

