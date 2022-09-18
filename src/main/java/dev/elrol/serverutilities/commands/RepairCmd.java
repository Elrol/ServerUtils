package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.CommandDelay;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class RepairCmd
extends _CmdBase {
    public RepairCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a).executes(this::execute)).then(Commands.literal("all").executes(c -> this.execute(c, true))));
        }
    }

    protected int execute(CommandContext<CommandSourceStack> c, boolean repairAll) {
        ServerPlayer player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            Main.textUtils.err(c, Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        if (!repairAll && !player.getMainHandItem().isDamageableItem()) {
            Main.textUtils.err(c, Errs.item_not_damageable());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                Main.textUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, repairAll), false);
        return 1;
    }

    public int execute(CommandContext<CommandSourceStack> c) {
        return this.execute(c, false);
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayer player;
        boolean repairAll;

        public CommandRunnable(ServerPlayer player, boolean repairAll) {
            this.player = player;
            this.repairAll = repairAll;
        }

        @Override
        public void run() {
            if (this.repairAll) {
                String id;
                int count = 0;
                for (ItemStack stack : this.player.getInventory().items) {
                    if (!stack.isEmpty() || !stack.isDamageableItem()) continue;
                    id = stack.getItem().getDescriptionId();
                    if (!id.isEmpty()) {
                        if (Main.blacklists.repair_blacklist.contains(id)) {
                            Main.textUtils.err(this.player, Errs.repair_blacklist(id));
                            continue;
                        }
                        stack.setDamageValue(0);
                        ++count;
                    }
                }
                Main.textUtils.msg(this.player, Msgs.all_item_repaired.get(String.valueOf(count)));
            } else {
                ItemStack stack = this.player.getMainHandItem();
                String resource = stack.getItem().getDescriptionId();
                if(!resource.isEmpty()) {
                    String id = resource.toString();
                    if (Main.blacklists.repair_blacklist.contains(id)) {
                        Main.textUtils.err(this.player, Errs.repair_blacklist(id));
                        return;
                    }
                    stack.setDamageValue(0);
                    Main.textUtils.msg(this.player, Msgs.item_repaired.get(stack.getDisplayName().getString()));
                }
            }
        }
    }

}

