package dev.elrol.serverutilities.init;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Msgs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerInit {
    private enum TimerStatus {
        STOPPED,
        INITIALIZING,
        RUNNING,
        STOPPING,
    }

    private static TimerStatus Status = TimerStatus.STOPPED;
    private static ScheduledThreadPoolExecutor EXECUTOR = null;
    private static final int EXECUTOR_SHUTDOWN_TIMEOUT = 10;
    private static final TimeUnit EXECUTOR_SHUTDOWN_TIME_UNIT = TimeUnit.SECONDS;

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

    private static final Runnable clearlagTask = () -> {
        Logger.debug("Clearlag Timer Task");
        boolean hostile = FeatureConfig.clearlag_hostile.get();
        boolean passive = FeatureConfig.clearlag_passive.get();
        boolean items = FeatureConfig.clearlag_items.get();

        Main.mcServer.getPlayerList().getPlayers().forEach(
                p -> Main.textUtils.msg(p, Msgs.wipe_warn.get(
                hostile ? "Hostile" : "&8&mHostile",
                passive ? "Passive" : "&8&mPassive",
                items ? "Item" : "&8&mItem"
        )));
        EXECUTOR.schedule(() -> Methods.clearlag(hostile, passive, items),1, TimeUnit.MINUTES);
    };

    public static void init() {
        if(Status != TimerStatus.STOPPED)
            Main.getLogger().warn("Attempting to initialize a TimerInit in the {} State", Status.name());
        Status = TimerStatus.INITIALIZING;
        EXECUTOR = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        EXECUTOR.scheduleAtFixedRate(secondTask, 1, 1, TimeUnit.SECONDS);
        EXECUTOR.scheduleAtFixedRate(minuteTask, 1, 1, TimeUnit.MINUTES);
        EXECUTOR.scheduleAtFixedRate(fiveMinuteTask, 5, 5, TimeUnit.MINUTES);

        if(FeatureConfig.auto_clearlag_enabled.get()) {
            int freq = FeatureConfig.clearlag_frequency.get();
            EXECUTOR.scheduleAtFixedRate(clearlagTask, freq, freq, TimeUnit.MINUTES);
        }
        Status = TimerStatus.RUNNING;
    }

    public static void shutdown() {
        if(Status != TimerStatus.RUNNING)
            Main.getLogger().warn("Attempting to stop a TimerInit in the {} State", Status.name());
        Status = TimerStatus.STOPPING;
        try {
            EXECUTOR.shutdownNow();
            if (EXECUTOR.awaitTermination(EXECUTOR_SHUTDOWN_TIMEOUT, EXECUTOR_SHUTDOWN_TIME_UNIT)) {
                Status = TimerStatus.STOPPED;
            } else {
                Main.getLogger().warn("TimerInit failed to shut down within {} {}", EXECUTOR_SHUTDOWN_TIMEOUT, EXECUTOR_SHUTDOWN_TIME_UNIT.name());
            }
        }
        catch (InterruptedException e) {
            Main.getLogger().warn("TimerInit shutdown was interrupted and may not have completed");
        }
    }
}
