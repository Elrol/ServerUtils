package com.github.elrol.elrolsutilities.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.libs.Logger;

import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CommandCooldown implements Runnable {

    private final ServerPlayerEntity player;
    public String cmd;
    public int seconds;
    private final ScheduledExecutorService s;
    private final ScheduledFuture<?> a;

    public CommandCooldown(ServerPlayerEntity player, int seconds, String cmd) {
        this.player = player;
        this.seconds = seconds;
        this.cmd = cmd;
        this.s = Executors.newSingleThreadScheduledExecutor();
        this.a = this.s.scheduleWithFixedDelay(this, 0L, 5L, TimeUnit.SECONDS);
        Logger.log("Starting Cooldown[" + cmd + "," + seconds + "]");
    }

    public static void init(ServerPlayerEntity player, int seconds, String name) {
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
            TextUtils.msg(player, Msgs.cooldownEnded(cmd));
            a.cancel(false);
        } else {
            this.seconds -= 5;
        }
    }

    public void cancel(){
        a.cancel(true);
        s.shutdown();
    }
}

