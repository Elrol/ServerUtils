package dev.elrol.serverutilities.libs;

import dev.elrol.serverutilities.Main;
import net.minecraft.ChatFormatting;

public class Logger {
    public static void log(String s) {
        Main.getLogger().info(ModInfo.getTag() + s);
    }

    public static void err(String s) {
        Main.getLogger().error(ModInfo.getTag() + ChatFormatting.RED + s);
    }

    public static void warn(String s) {
        Main.getLogger().warn(ModInfo.getTag() + ChatFormatting.YELLOW + s);
    }

    public static void debug(String s) {
        if (Main.isDev()) {
            Main.getLogger().info(ModInfo.getTag() + "[DEBUG]" + s);
        }
    }
}

