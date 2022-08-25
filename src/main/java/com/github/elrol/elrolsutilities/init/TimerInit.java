package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.libs.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerInit {

    private static final ScheduledThreadPoolExecutor EXECUTOR = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    private static final Runnable secondTask = () -> {
        //Logger.debug("Second Task");
        Main.serverData.tickJails();
    };

    private static final Runnable minuteTask = () -> {
        Logger.debug("Minute Task");
        Main.serverData.tickMutes();
        if(Main.bot.bot != null) {
            Main.bot.update();
        }
    };
    private static final Runnable fiveMinuteTask = () -> {
        Logger.debug("Five Minute Task");
        if(FeatureConfig.enable_economy.get()) Main.shopRegistry.save();
    };

    public static void init() {
        EXECUTOR.scheduleAtFixedRate(secondTask, 1, 1, TimeUnit.SECONDS);
        EXECUTOR.scheduleAtFixedRate(minuteTask, 1, 1, TimeUnit.MINUTES);
        EXECUTOR.scheduleAtFixedRate(fiveMinuteTask, 5, 5, TimeUnit.MINUTES);
    }

    public static void shutdown() {
        EXECUTOR.shutdownNow();
    }

}
