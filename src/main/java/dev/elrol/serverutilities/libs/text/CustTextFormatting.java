package dev.elrol.serverutilities.libs.text;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.config.FeatureConfig;
import net.minecraft.ChatFormatting;

import java.util.Map;

public class CustTextFormatting {
    public static final String rainbow = "4c6e2ab319d5";

    public static String format(String f, String string) {
        Main.getLogger().info("Color List: " + f.toString());
        int count = 0;
        Map<Character, ChatFormatting> colors = Main.textUtils.getColors();
        StringBuilder text = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (count >= f.length()) {
                count = 0;
            }
            Character id = f.charAt(count);
            if(colors.containsKey(id))
                text.append(colors.get(id));
            else
                text.append(colors.get('f'));
            text.append(c);
            ++count;
        }
        return text.toString();
    }

    public static String rainbow(String string) {
        return format(rainbow, string);
    }

    public static String jan(String string) { return format(FeatureConfig.jan_colors.get(), string); }

    public static String feb(String string) {
        return format(FeatureConfig.feb_colors.get(), string);
    }

    public static String mar(String string) {
        return format(FeatureConfig.mar_colors.get(), string);
    }

    public static String apr(String string) {
        return format(FeatureConfig.apr_colors.get(), string);
    }

    public static String may(String string) {
        return format(FeatureConfig.may_colors.get(), string);
    }

    public static String jun(String string) {
        return format(FeatureConfig.jun_colors.get(), string);
    }

    public static String jul(String string) {
        return format(FeatureConfig.jul_colors.get(), string);
    }

    public static String aug(String string) {
        return format(FeatureConfig.aug_colors.get(), string);
    }

    public static String sep(String string) {
        return format(FeatureConfig.sep_colors.get(), string);
    }

    public static String oct(String string) {
        return format(FeatureConfig.oct_colors.get(), string);
    }

    public static String nov(String string) {
        return format(FeatureConfig.nov_colors.get(), string);
    }

    public static String dec(String string) {
        return format(FeatureConfig.dec_colors.get(), string);
    }
}

