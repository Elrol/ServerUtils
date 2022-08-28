package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.CommandDelay;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class RepairCmd
extends _CmdBase {
    public RepairCmd(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        super(delay, cooldown, aliases, cost);
    }

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (String a : aliases) {
            if(name.isEmpty()) name = a;
                dispatcher.register((Commands.literal(a).executes(this::execute)).then(Commands.literal("all").executes(c -> this.execute(c, true))));
        }
    }

    protected int execute(CommandContext<CommandSource> c, boolean repairAll) {
        ServerPlayerEntity player;
        try {
            player = c.getSource().getPlayerOrException();
        } catch (CommandSyntaxException e) {
            TextUtils.err(c, Errs.not_player());
            return 0;
        }
        if (Methods.hasCooldown(player, this.name)) {
            return 0;
        }
        if (!repairAll && !player.getMainHandItem().isDamageableItem()) {
            TextUtils.err(c, Errs.item_not_damageable());
            return 0;
        }
        IPlayerData data = Main.database.get(player.getUUID());
        if (FeatureConfig.enable_economy.get() && cost > 0) {
            if (!data.charge(cost)) {
                TextUtils.err(player, Errs.not_enough_funds(this.cost, data.getBal()));
                return 0;
            }
        }
        CommandDelay.init(this, player, new CommandRunnable(player, repairAll), false);
        return 1;
    }

    @Override
    public int execute(CommandContext<CommandSource> c) {
        return this.execute(c, false);
    }

    private static class CommandRunnable
    implements Runnable {
        ServerPlayerEntity player;
        boolean repairAll;

        public CommandRunnable(ServerPlayerEntity player, boolean repairAll) {
            this.player = player;
            this.repairAll = repairAll;
        }

        @Override
        public void run() {
            if (this.repairAll) {
                ResourceLocation id;
                int count = 0;
                for (ItemStack stack : this.player.inventory.items) {
                    if (!stack.isEmpty() || !stack.isDamageableItem()) continue;
                    id = stack.getItem().getRegistryName();
                    if (id != null) {
                        if (Main.blacklists.repair_blacklist.contains(id.toString())) {
                            TextUtils.err(this.player, Errs.repair_blacklist(id.toString()));
                            continue;
                        }
                        stack.setDamageValue(0);
                        ++count;
                    }
                }
                TextUtils.msg(this.player, Msgs.all_item_repaired.get("" + count));
            } else {
                ItemStack stack = this.player.getMainHandItem();
                ResourceLocation resource = stack.getItem().getRegistryName();
                if(resource != null) {
                    String id = resource.toString();
                    if (Main.blacklists.repair_blacklist.contains(id)) {
                        TextUtils.err(this.player, Errs.repair_blacklist(id));
                        return;
                    }
                    stack.setDamageValue(0);
                    TextUtils.msg(this.player, Msgs.item_repaired.get(stack.getDisplayName().getString()));
                }
            }
        }
    }

}

