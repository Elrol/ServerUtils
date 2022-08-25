package com.github.elrol.elrolsutilities.data;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CommandCooldown implements Runnable {

    private static final ScheduledThreadPoolExecutor EXECUTOR = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);


    private final ServerPlayer player;
    public String cmd;
    public int seconds;
    private ScheduledFuture<?> a;

    public CommandCooldown(ServerPlayer player, int seconds, String cmd) {
        this.player = player;
        this.seconds = seconds;
        this.cmd = cmd;
        this.a = EXECUTOR.scheduleWithFixedDelay(this, 0L, 5L, TimeUnit.SECONDS);
        Logger.log("Starting Cooldown[" + cmd + "," + seconds + "]");
    }

    public static void init(ServerPlayer player, int seconds, String name) {
        if (player == null) {
            return;
        }
        if (Main.commandCooldowns.containsKey(player.getUUID())) {
            Logger.log("Player Cooldown Map exists");
            Map<String, CommandCooldown> cmds = Main.commandCooldowns.get(player.getUUID());
            if (cmds == null) {
                cmds = new HashMap<>();
            }
            cmds.put(name, new CommandCooldown(player, seconds, name));
            Main.commandCooldowns.replace(player.getUUID(), cmds);
        } else {
            Logger.log("Player Cooldown Map doesnt exists");
            Map<String, CommandCooldown> cmds = new HashMap<>();
            cmds.put(name, new CommandCooldown(player, seconds, name));
            Main.commandCooldowns.put(player.getUUID(), cmds);
        }
    }

    public void run(){
        if(this.seconds < 1){
            Main.commandCooldowns.remove(player.getUUID()).remove(cmd);
            TextUtils.msg(player, Msgs.cooldownEnded.get(cmd));
            a.cancel(false);
        } else {
            this.seconds -= 5;
        }
    }

    public void cancel(){
        a.cancel(true);
    }

    public static void shutdown() {
        EXECUTOR.shutdownNow();
    }
}

