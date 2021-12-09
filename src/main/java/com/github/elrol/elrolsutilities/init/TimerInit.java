package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.Main;

import java.util.Timer;
import java.util.TimerTask;

public class TimerInit {
    private static final Timer timer = new Timer();

    public static void init() {
        timer.scheduleAtFixedRate(secondTask, 1000L, 1000L);
        timer.scheduleAtFixedRate(minuteTask, 60000L, 60000L);
        timer.scheduleAtFixedRate(fiveMinuteTask, 300000L, 300000L);
    }

    public static void shutdown() {
        Main.commandCooldowns.forEach((uuid, map) -> {
            map.forEach((cmd, cooldown) -> {
                cooldown.cancel();
            });
        });
        timer.cancel();
    }

    private static final TimerTask secondTask = new TimerTask() {
            @Override
            public void run() {
                //Main.getLogger().debug("Second Task Ran");
                Main.serverData.tickJails();
            }
    };

    private static final TimerTask minuteTask = new TimerTask() {
        @Override
        public void run() {
            //Main.getLogger().debug("Minute Task Ran");
            Main.serverData.tickMutes();
        }
    };

    private static final TimerTask fiveMinuteTask = new TimerTask() {
        @Override
        public void run() {
            Main.shopRegistry.save();
        }
    };

}
