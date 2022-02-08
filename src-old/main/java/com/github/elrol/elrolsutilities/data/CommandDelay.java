package com.github.elrol.elrolsutilities.data;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.commands._CmdBase;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class CommandDelay implements Runnable {
    private ServerPlayerEntity player;
    private Runnable runnable;
    public int seconds;
    public int cooldown;
    public String name;
    public BlockPos pos = null;
    private ScheduledExecutorService s;
    private ScheduledFuture<?> a;

    public CommandDelay(ServerPlayerEntity player, String name, int seconds, int cooldown, Runnable runnable) {
        Logger.debug("Command Delay Started");
        this.player = player;
        this.runnable = runnable;
        this.seconds = seconds;
        this.cooldown = cooldown;
        this.name = name;
        Logger.log("CommandDelay[" + name + "] { Delay[" + seconds + "], Cooldown[" + cooldown + "]");
        if (seconds > 0) {
            this.s = Executors.newSingleThreadScheduledExecutor();
            this.a = this.s.scheduleWithFixedDelay(this, 0L, 1L, TimeUnit.SECONDS);
        } else {
            this.run();
        }
    }

    public CommandDelay(ServerPlayerEntity player, String name, int seconds, int cooldown, Runnable runnable, boolean trackLoc) {
        Logger.debug("Command Delay Started");
        this.player = player;
        this.runnable = runnable;
        this.seconds = seconds;
        this.cooldown = cooldown;
        this.name = name;
        if (trackLoc) {
            this.pos = new BlockPos(player.position());
        }
        //Logger.log("CommandDelay[" + name + "] { Delay[" + seconds + "], Cooldown[" + cooldown + "]");
        if (seconds > 0) {
            this.s = Executors.newSingleThreadScheduledExecutor();
            this.a = this.s.scheduleWithFixedDelay(this, 0L, 1L, TimeUnit.SECONDS);
        } else {
            this.run();
        }
    }

    public static void init(ServerPlayerEntity sender, int time, Runnable runnable, boolean trackLoc) {
        if (sender == null) {
            return;
        }
        if (Main.commandDelays.containsKey(sender.getUUID())) {
            CommandDelay delay = Main.commandDelays.get(sender.getUUID());
            if (delay.seconds > 0) {
                TextUtils.err(sender, Errs.delay_running());
                return;
            }
            Main.commandDelays.remove(sender.getUUID());
        }
        Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, "", time, 0, runnable, trackLoc));
    }

    public static void init(_CmdBase cmd, CommandSource source, Runnable runnable, boolean trackLoc) {
        ServerPlayerEntity sender;
        try{
            sender = source.getPlayerOrException();
        } catch (CommandSyntaxException e){
            sender = null;
        }
        if (sender == null) {
            runnable.run();
            return;
        }
        init(cmd, sender, runnable, trackLoc);
    }

    public static void init(_CmdBase cmd, ServerPlayerEntity sender, Runnable runnable, boolean trackLoc){
        if (Methods.hasCooldown(sender, cmd.name)) {
            return;
        }
        if (Main.commandDelays.containsKey(sender.getUUID())) {
            CommandDelay delay = Main.commandDelays.get(sender.getUUID());
            if (delay.seconds > 0) {
                TextUtils.err(sender, Errs.delay_running());
                return;
            }
            Main.commandDelays.remove(sender.getUUID());
        }

        Logger.debug("Delay: " + cmd.delay + ", Cooldown: " + cmd.cooldown);
        if(cmd.cooldown > 0) {
            Logger.debug("Cooldown is greater then 0 ");
            if(cmd.delay > 0){
                Logger.debug("Delay is greater then 0");
                Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, cmd.name, cmd.delay, cmd.cooldown, runnable, trackLoc));
            } else {
                Logger.debug("Delay is 0");
                Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, cmd.name, 0, cmd.cooldown, runnable, trackLoc));
            }
        } else {
            Logger.debug("Cooldown is 0");
            if(cmd.delay > 0){
                Logger.debug("Delay is greater then 0");
                Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, cmd.name, cmd.delay, 0, runnable, trackLoc));
            } else {
                Logger.debug("Delay is 0");
                Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, cmd.name, 0, 0, runnable, trackLoc));
            }
        }
    }

    public void cancel() {
        if (this.a != null) {
            this.a.cancel(true);
        }
        if(s != null) s.shutdown();
        Main.commandDelays.remove(this.player.getUUID());
    }

    @Override
    public void run() {
        if (seconds <= 0) {
            Logger.debug("Running command runable");
            this.runnable.run();
            if (a != null) {
                a.cancel(false);
            }
            Main.commandDelays.remove(this.player.getUUID());
            if (cooldown > 0) {
                Logger.log("Cooldown starting from commandDelays");
                CommandCooldown.init(player, cooldown, name);
            }
        } else {
            --this.seconds;
            TextUtils.msg(this.player.createCommandSource(), "" + (this.seconds + 1));
        }
    }
}

