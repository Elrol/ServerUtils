package dev.elrol.serverutilities.commands;

import dev.elrol.serverutilities.api.commands.ICmdBase;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public abstract class _CmdBase implements ICmdBase {
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

}

