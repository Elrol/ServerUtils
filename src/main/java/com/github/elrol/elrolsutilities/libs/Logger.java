package com.github.elrol.elrolsutilities.libs;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import net.minecraft.ChatFormatting;

public class Logger {
    public static void log(String s) {
        if(FeatureConfig.tag == null)
            Main.getLogger().info(s);
        else
            Main.getLogger().info(ModInfo.getTag() + s);
    }

    public static void err(String s) {
        Main.getLogger().error(ModInfo.getTag() + ChatFormatting.RED + s);
    }

    public static void debug(String s) {
        if (Main.isDev()) {
            Main.getLogger().info(ModInfo.getTag() + "[DEBUG]" + s);
        }
    }
}

