package dev.elrol.serverutilities.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.ClaimBlock;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.data.Permission;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClaimCmd extends _CmdBase {
    public ClaimCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : this.aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a)
                        .executes(c-> execute(c, 0))
                        .then(Commands.argument("radius", IntegerArgumentType.integer(0))
                                .executes(c-> execute(c, IntegerArgumentType.getInteger(c,"radius"))))));
        }
        new Permission(CommandConfig.claim_bypass_perm.get());
        new Permission(CommandConfig.claim_max_perm.get());
    }

    protected int execute(CommandContext<CommandSourceStack> c, int radius) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            Main.textUtils.err(c.getSource(), Errs.not_player());
            return 0;
        }
        if(radius == 0) {
            IPlayerData data = Main.database.get(player.getUUID());
            if (FeatureConfig.enable_economy.get() && this.cost > 0) {
                if (!data.charge(this.cost)) {
                    Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                    return 0;
                }
            }
            ClaimBlock claim = new ClaimBlock(player);
            if (Main.serverData.isClaimed(claim)) {
                UUID uuid = Main.serverData.getOwner(claim);
                IPlayerData d = Main.database.get(uuid);
                Main.textUtils.err(player, Errs.chunk_claimed(d.getDisplayName()));
                return 0;
            }
            if (Main.serverData.getClaims(player.getUUID()).size() >= data.getMaxClaims()) {
                Main.textUtils.err(player, Errs.max_claim());
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, radius), false);
        return 1;
    }

    private static class CommandRunnable implements Runnable {
        ServerPlayer player;
        int range;

        public CommandRunnable(ServerPlayer target, int range) {
            this.player = target;
            this.range = Math.max(range, 0);
        }

        @Override
        public void run() {
            if(range == 0) {
                Main.textUtils.msg(this.player, Msgs.chunk_claimed.get());
                Main.serverData.claim(new ClaimBlock(player), player.getUUID());
            } else {
                IPlayerData data = Main.database.get(player.getUUID());
                List<ClaimBlock> claims = new ArrayList<>();

                int cost = CommandConfig.claim.cost.get();
                int originX = player.chunkPosition().x;
                int originZ = player.chunkPosition().z;

                int errs = 0;

                int cur = Main.serverData.getClaims(player.getUUID()).size();
                int d = (range * 2) + 1;
                int area = d * d;
                int max = data.getMaxClaims();

                for(int x = -range; x <= range; x++) {
                    for(int z = -range; z <= range; z++) {
                        ClaimBlock claim = new ClaimBlock(player.level.dimension().location(),
                                originX + x,
                                originZ + z);
                        if (Main.serverData.isClaimed(claim)) {
                            errs++;
                            continue;
                        }
                        claims.add(claim);
                    }
                }

                int num = claims.size();
                int totalCost = cost * num;

                if(cur + num >= max){
                    Main.textUtils.err(player, Errs.not_enough_claims.get(String.valueOf((cur+area)-max)));
                    return;
                }

                if(FeatureConfig.enable_economy.get() && totalCost > 0){
                    if(!data.charge(totalCost)){
                        Main.textUtils.err(player, Errs.not_enough_funds(totalCost, data.getBal()));
                        return;
                    }
                }
                claims.forEach(claim -> Main.serverData.claim(claim, player.getUUID()));
                Main.textUtils.msg(this.player, Msgs.chunks_claimed.get(String.valueOf(num), Main.textUtils.parseCurrency(totalCost, false)));
            }
            Main.serverData.save();
        }
    }

}

