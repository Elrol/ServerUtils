package dev.elrol.serverutilities.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.commands._CmdBase;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CommandDelay
implements Runnable {
    private static final ScheduledThreadPoolExecutor s = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    private final ServerPlayer player;
    private final Runnable runnable;

    public int seconds;
    public int cooldown;
    public String name;
    public BlockPos pos = null;
    private ScheduledFuture<?> a;

    public CommandDelay(ServerPlayer player, String name, int seconds, int cooldown, Runnable runnable) {
        Logger.debug("Command Delay Started");
        this.player = player;
        this.runnable = runnable;
        this.seconds = seconds;
        this.cooldown = cooldown;
        this.name = name;
        Logger.log("CommandDelay[" + name + "] { Delay[" + seconds + "], Cooldown[" + cooldown + "]");
        if (seconds > 0) {
            a = s.scheduleWithFixedDelay(this, 0L, 1L, TimeUnit.SECONDS);
        } else {
            this.run();
        }
    }

    public CommandDelay(ServerPlayer player, String name, int seconds, int cooldown, Runnable runnable, boolean trackLoc) {
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
            this.a = s.scheduleWithFixedDelay(this, 0L, 1L, TimeUnit.SECONDS);
        } else {
            this.run();
        }
    }

    public static void init(ServerPlayer sender, int time, Runnable runnable, boolean trackLoc) {
        if (sender == null) {
            return;
        }
        if (Main.commandDelays.containsKey(sender.getUUID())) {
            CommandDelay delay = Main.commandDelays.get(sender.getUUID());
            if (delay.seconds > 0) {
                Main.textUtils.err(sender, Errs.delay_running());
                return;
            }
            Main.commandDelays.remove(sender.getUUID());
        }
        Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, "", time, 0, runnable, trackLoc));
    }

    public static void init(_CmdBase cmd, CommandSourceStack source, Runnable runnable, boolean trackLoc) {
        ServerPlayer sender;
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

    public static void init(String cmdName, int cmdDelay, int cmdCooldown, ServerPlayer sender, Runnable runnable, boolean trackLoc) {
        if (Methods.hasCooldown(sender, cmdName)) {
            return;
        }
        if (Main.commandDelays.containsKey(sender.getUUID())) {
            CommandDelay delay = Main.commandDelays.get(sender.getUUID());
            if (delay.seconds > 0) {
                Main.textUtils.err(sender, Errs.delay_running());
                return;
            }
            Main.commandDelays.remove(sender.getUUID());
        }

        Logger.debug("Delay: " + cmdDelay + ", Cooldown: " + cmdCooldown);
        if(cmdCooldown > 0) {
            Logger.debug("Cooldown is greater then 0 ");
            if(cmdDelay > 0){
                Logger.debug("Delay is greater then 0");
                Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, cmdName, cmdDelay, cmdCooldown, runnable, trackLoc));
            } else {
                Logger.debug("Delay is 0");
                Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, cmdName, 0, cmdCooldown, runnable, trackLoc));
            }
        } else {
            Logger.debug("Cooldown is 0");
            if(cmdDelay > 0){
                Logger.debug("Delay is greater then 0");
                Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, cmdName, cmdDelay, 0, runnable, trackLoc));
            } else {
                Logger.debug("Delay is 0");
                Main.commandDelays.put(sender.getUUID(), new CommandDelay(sender, cmdName, 0, 0, runnable, trackLoc));
            }
        }

    }

    public static void init(_CmdBase cmd, ServerPlayer sender, Runnable runnable, boolean trackLoc){
        init(cmd.name, cmd.delay,cmd.cooldown, sender, runnable, trackLoc);
    }

    public void cancel() {
        if (this.a != null) {
            this.a.cancel(true);
        }
        Main.commandDelays.remove(this.player.getUUID());
    }

    public static void shutdown() {
        if(s != null)
            s.shutdown();
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
            Main.textUtils.action(player, Component.literal(seconds + " Seconds Left"));
            --seconds;
        }
    }
}

