package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.Main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerInit {

    private static final ScheduledThreadPoolExecutor EXECUTOR = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    private static final Runnable secondTask = () -> Main.serverData.tickJails();
    private static final Runnable minuteTask = () -> {
        Main.serverData.tickMutes();
        if(Main.bot.bot != null) {
            Main.bot.update();
        }
    };
    private static final Runnable fiveMinuteTask = () -> Main.shopRegistry.save();

    public static void init() {
        EXECUTOR.scheduleAtFixedRate(secondTask, 1, 1, TimeUnit.SECONDS);
        EXECUTOR.scheduleAtFixedRate(minuteTask, 1, 1, TimeUnit.MINUTES);
        EXECUTOR.scheduleAtFixedRate(fiveMinuteTask, 5, 5, TimeUnit.MINUTES);
    }

    public static void shutdown() {
        EXECUTOR.shutdownNow();
    }

}
