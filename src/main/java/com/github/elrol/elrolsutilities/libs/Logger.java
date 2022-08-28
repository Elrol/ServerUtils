package com.github.elrol.elrolsutilities.libs;

import com.github.elrol.elrolsutilities.Main;
import net.minecraft.util.text.TextFormatting;

public class Logger {

    public static void log(String s) {
        Main.getLogger().info(ModInfo.getTag() + s);
    }

    public static void err(String s) {
        Main.getLogger().error(ModInfo.getTag() + TextFormatting.RED + s);
    }

    public static void warn(String s) {
        Main.getLogger().warn(ModInfo.getTag() + TextFormatting.YELLOW + s);
    }

    public static void debug(String s) {
        if (Main.isDev()) {
            Main.getLogger().info(ModInfo.getTag() + "[DEBUG]" + s);
        }
    }
}

