package com.github.elrol.elrolsutilities.commands;

import com.github.elrol.elrolsutilities.api.perms.IPermission;
import com.github.elrol.elrolsutilities.data.Permission;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandSource;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class _CmdBase {
    public String name = "";
    public List<String> aliases = new ArrayList<>();
    public int delay;
    public int cooldown;
    public int cost;

    public _CmdBase(ForgeConfigSpec.IntValue delay, ForgeConfigSpec.IntValue cooldown, ForgeConfigSpec.ConfigValue<List<? extends String>> aliases, ForgeConfigSpec.IntValue cost) {
        this.delay = delay.get();
        this.cooldown = cooldown.get();
        this.aliases.addAll(aliases.get());
        this.cost = cost.get();

    }

    public _CmdBase(int delay, int cooldown, List<String> aliases, int cost) {
        this.delay = delay;
        this.cooldown = cooldown;
        this.aliases = aliases;
        this.cost = cost;
    }

    protected abstract void register(CommandDispatcher<CommandSource> var1);
    protected abstract int execute(CommandContext<CommandSource> var1);
}

