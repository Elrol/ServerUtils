package com.github.elrol.elrolsutilities.libs.text;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class TextUtils {
    private static int dev;

    public static String ticksToTime(long ticks) {
        long years = ticks / 525600;
        long weeks = (ticks % 525600) / 10080;
        long days = (ticks % 10080) / 1440;
        long hours = (ticks) / 60;
        long min = ticks % 60;
        String time = "";
        if(years > 0) time += years + ":";
        if(weeks > 0) time += weeks + ":";
        if(days > 0) time += days + ":";
        if(hours > 0) time += days + ":";
        return time + min;
    }

    public static void staffChat(String message, UUID sender) {
        StringBuilder format = new StringBuilder("&r");
        for(char c : FeatureConfig.sc_format.get().toCharArray()) {
            format.append("&").append(c);
        }
        message = message.replace("&r", format);
        String text = formatString(message);
        Main.serverData.staffList.forEach(uuid -> {
            ServerPlayerEntity staff = Methods.getPlayerFromUUID(uuid);
            staff.sendMessage(new StringTextComponent(text), sender);
        });
    }

    public static void sendToStaff(CommandSource source, String message) {
        String name = Methods.getDisplayName(source);
        ServerPlayerEntity player;
        UUID sender;
        String tag = FeatureConfig.sc_tag.get();
        try{
            player = source.getPlayerOrException();
            PlayerData data = Main.database.get(player.getUUID());
            if(data.isJailed()) tag = FeatureConfig.sc_jail_tag.get();
            sender = player.getUUID();
        } catch (CommandSyntaxException e) {
            sender = UUID.randomUUID();
        }
        staffChat(tag + "&r " + name + "&r: " + message, sender);
    }

    public static String listToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for(String s : list) {
            if(builder.toString().isEmpty()) builder.append(s);
            else builder.append(", ").append(s);
        }
        return builder.toString();
    }

    public static StringTextComponent commandHelp(String[] content) {
        StringTextComponent text = new StringTextComponent(formatString(FeatureConfig.command_help_spacer.get() + "\n"));
        for(String s : content){
            if(s.contains(":")) {
                String[] temp = s.split(":");
                text.append(formatString(FeatureConfig.command_help_entry.get().replace("KEY", temp[0]).replace("VALUE", temp[1])) + "\n");
            } else {
                text.append(formatString(FeatureConfig.command_help_info.get().replace("INFO", s)) + "\n");
            }
        }
        text.append(formatString(FeatureConfig.command_help_spacer.get()));
        return text;
    }

    public static StringTextComponent formatChat(ServerPlayerEntity player, String msg){
        PlayerData data = Main.database.get(player.getUUID());
        StringTextComponent text = new StringTextComponent("");
        if (data.isPatreon) text.append(TextUtils.formatString("&5[&dPatreon&5]") + TextFormatting.RESET + " ");
        if (!data.getPrefix().isEmpty()) {
            String prefix = TextUtils.formatString(data.getPrefix());
            text.append(prefix + TextFormatting.RESET + " ");
        }
        if (data.nickname == null || data.nickname.isEmpty()) {
            text.append(player.getDisplayName().getString());
        } else {
            text.append(TextUtils.formatString(data.nickname) + TextFormatting.RESET);
        }
        if (!data.getSuffix().isEmpty()) {
            String suffix = TextUtils.formatString(data.getSuffix());
            text.append(suffix);
        }
        text.append(": ");
        if (FeatureConfig.color_chat_enable.get()) {
            if (IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.createCommandSourceStack(), FeatureConfig.color_chat_perm.get())) {
                Logger.debug("Color chat enabled");
                text.append(TextUtils.format(msg));
            } else {
                Logger.debug("Doesnt have permission to use color chat");
                text.append(msg);
            }
        } else {
            Logger.debug("Color chat disabled");
            text.append(msg);
        }
        return text;
    }

    public static void msg(CommandContext<CommandSource> context, TextComponent translation) {
        TextUtils.msg(context.getSource(), translation);
    }

    public static void msg(ServerPlayerEntity player, TextComponent translation) {
        TextUtils.msg(player.createCommandSourceStack(), translation);
    }

    public static void msg(CommandSource source, String text) {
        source.sendSuccess(new StringTextComponent(ModInfo.getTag() + text), false);
    }

    public static void msgNoTag(CommandSource source, TextComponent text){
        source.sendSuccess(text, false);
    }

    public static void msg(CommandSource source, TextComponent translation) {
        source.sendSuccess(new StringTextComponent(ModInfo.getTag()).append(translation), false);
    }

    public static void err(CommandContext<CommandSource> context, TextComponent translation) {
        TextUtils.err(context.getSource(), translation);
    }

    public static void err(ServerPlayerEntity player, TextComponent translation) {
        TextUtils.err(player.createCommandSourceStack(), translation);
    }

    public static void err(CommandSource source, String text) {
        source.sendFailure(new StringTextComponent(ModInfo.getTag() + TextFormatting.RED + text));
    }

    public static void err(CommandSource source, TextComponent translation) {
        source.sendFailure(new StringTextComponent(ModInfo.getTag() + TextFormatting.RED).append(translation));
    }

    public static void tab_msg(CommandSource source, String string) {
        String tab = "    ";
        source.sendFailure(new StringTextComponent(tab + string));
    }

    public static void msg(CommandSource source, String[] stringArray) {
        for (int i = 0; i < stringArray.length; ++i) {
            if (i == 0) {
                source.sendFailure(new StringTextComponent(ModInfo.getTag() + stringArray[0]));
                continue;
            }
            TextUtils.tab_msg(source, stringArray[i]);
        }
    }

    public static String holiday(String string) {
        int month = Calendar.getInstance().get(2);
        Main.getLogger().info("The month is: " + month);
        switch (month) {
            case 0: return CustTextFormatting.jan(string);
            case 1: return CustTextFormatting.feb(string);
            case 2: return CustTextFormatting.mar(string);
            case 3: return CustTextFormatting.apr(string);
            case 4: return CustTextFormatting.may(string);
            case 5: return CustTextFormatting.jun(string);
            case 6: return CustTextFormatting.jul(string);
            case 7: return CustTextFormatting.aug(string);
            case 8: return CustTextFormatting.sep(string);
            case 9: return CustTextFormatting.oct(string);
            case 10: return CustTextFormatting.nov(string);
            case 11: return CustTextFormatting.dec(string);
        }
        return string;
    }

    public static String format(String string){
        StringBuilder output = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        if(string.contains("http")){
            for(String s : string.split(" ")){
                if(s.startsWith("http")) {
                    output.append(formatString(builder.toString()));
                    output.append(s).append(" ");
                    builder = new StringBuilder();
                    Logger.debug("found http");
                }
                else {
                    builder.append(s).append(" ");
                    Logger.debug("not http");
                }
            }
        } else {
            builder.append(string);
        }
        return output.append(formatString(builder.toString())).toString();
    }


    public static String formatString(String string) {
        if(string == null) return "";
        string = string.replace("\\n", "\n");
        new StringTextComponent(string);
        StringBuilder text = new StringBuilder();
        Map<Character, TextFormatting> colors = getColors();
        if(string.isEmpty() || !string.contains("&")) return string;
        String[] splitString = string.split("&");
        for (int i = 0; i < splitString.length; ++i) {
            if (i == 0) {
                text.append(splitString[i]);
                continue;
            }
            String split = splitString[i];
            if (split.isEmpty()) continue;
            char id = split.charAt(0);
            if(colors.containsKey(id)){
                split = colors.get(id) + split.substring(1);
            } else if(id == 'g' && FeatureConfig.rainbow_code_enable.get()) {
                split = CustTextFormatting.rainbow(split.substring(1));
            } else if(id == 'h' && FeatureConfig.holiday_code_enable.get()) {
                split = TextUtils.holiday(split.substring(1));
            } else {
                split = "&" + split;
            }
            text.append(split);
        }
        return text.toString();
    }

    public static void sendMessage(CommandSource source, ServerPlayerEntity player, String msg) {
        String message = TextFormatting.DARK_GRAY + "[" + TextFormatting.GRAY;
        PlayerData pData = Main.database.get(player.getUUID());
        UUID uuid = UUID.randomUUID();
        try {
            ServerPlayerEntity p = source.getPlayerOrException();
            uuid = p.getUUID();
            PlayerData sData = Main.database.get(p.getUUID());
            sData.lastMsg = player.getUUID();
            pData.lastMsg = p.getUUID();
            Logger.debug("Source was a player");
        } catch (CommandSyntaxException e) {
            Logger.debug("Source was not player");
        }
        message = message + Methods.getDisplayName(source);
        message = message + TextFormatting.DARK_GRAY + " >> " + TextFormatting.GRAY;
        message = message + Methods.getDisplayName(player.createCommandSourceStack());
        message = message + TextFormatting.DARK_GRAY + "] " + TextFormatting.GRAY;
        message = FeatureConfig.color_chat_enable.get() && IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.createCommandSourceStack(), FeatureConfig.color_chat_perm.get()) ? message + TextUtils.formatString(msg) : message + msg;
        player.sendMessage(new StringTextComponent(message), uuid);
        source.sendSuccess(new StringTextComponent(message), false);
    }

    public static final Map<Character, TextFormatting> getColors() {
        Map<Character, TextFormatting> map = new HashMap<>();

        map.put('0', TextFormatting.BLACK);
        map.put('1', TextFormatting.DARK_BLUE);
        map.put('2', TextFormatting.DARK_GREEN);
        map.put('3', TextFormatting.DARK_AQUA);
        map.put('4', TextFormatting.DARK_RED);
        map.put('5', TextFormatting.DARK_PURPLE);
        map.put('6', TextFormatting.GOLD);
        map.put('7', TextFormatting.GRAY);
        map.put('8', TextFormatting.DARK_GRAY);
        map.put('9', TextFormatting.BLUE);
        map.put('a', TextFormatting.GREEN);
        map.put('b', TextFormatting.AQUA);
        map.put('c', TextFormatting.RED);
        map.put('d', TextFormatting.LIGHT_PURPLE);
        map.put('e', TextFormatting.YELLOW);
        map.put('f', TextFormatting.WHITE);

        map.put('k', TextFormatting.OBFUSCATED);
        map.put('l', TextFormatting.BOLD);
        map.put('m', TextFormatting.STRIKETHROUGH);
        map.put('n', TextFormatting.UNDERLINE);
        map.put('o', TextFormatting.ITALIC);
        map.put('r', TextFormatting.RESET);

        return map;
    }

    public static String stringToGolden(String parString, int parShineLocation, boolean parReturnToBlack){
        int stringLength = parString.length();
        if (stringLength < 1){
            return "";
        }
        StringBuilder outputString = new StringBuilder();
        for (int i = 0; i < stringLength; i++){
            if ((i+parShineLocation + Main.mcServer.getNextTickTime()/20)%88==0){
                outputString.append(TextFormatting.WHITE).append(parString, i, i + 1);
            } else if ((i+parShineLocation + Main.mcServer.getNextTickTime()/20)%88==1){
                outputString.append(TextFormatting.YELLOW).append(parString, i, i + 1);
            } else if ((i + parShineLocation+ Main.mcServer.getNextTickTime()/20)%88==87){
                outputString.append(TextFormatting.YELLOW).append(parString, i, i + 1);
            } else {
                outputString.append(TextFormatting.GOLD).append(parString, i, i + 1);
            }
        }
        if (parReturnToBlack){
            return outputString.toString() +TextFormatting.BLACK;
        }
        return outputString.toString() + TextFormatting.WHITE;
}

    /***
     *
     * @param amount The amount of currency to format
     * @param symbol If true, parses using symbol, otherwise uses singular/plural
     * @return The amount formatted to be readable
     */
    public static String parseCurrency(double amount, boolean symbol) {
        String formatted = String.format("%.2f", amount);

        if(!symbol){
            if(amount == 1) return formatted + " " + FeatureConfig.currency_singular.get();
            return formatted + " " + FeatureConfig.currency_plural.get();
        }
        return formatted + FeatureConfig.currency_symbol.get();
    }
}

