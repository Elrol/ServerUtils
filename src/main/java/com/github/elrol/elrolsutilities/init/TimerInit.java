package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.Main;

import java.util.Timer;
import java.util.TimerTask;

public class TimerInit {

    public static void init() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(secondTask, 1000L, 1000L);
        timer.scheduleAtFixedRate(minuteTask, 60000L, 60000L);
    }

    private static TimerTask secondTask = new TimerTask() {
            @Override
            public void run() {
                //Main.getLogger().debug("Second Task Ran");
                Main.serverData.tickJails();
            }
    };

    private static TimerTask minuteTask = new TimerTask() {
        @Override
        public void run() {
            //Main.getLogger().debug("Minute Task Ran");
            Main.serverData.tickMutes();
        }
    };

}
