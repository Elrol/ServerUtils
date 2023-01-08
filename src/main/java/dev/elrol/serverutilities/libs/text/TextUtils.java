package dev.elrol.serverutilities.libs.text;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.init.ITextUtils;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.ModInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class TextUtils implements ITextUtils {

    @Override
    public Component formatComponent(String input) {
        if(!input.contains("&")) return Component.literal(input);
        String[] split = input.split("&");
        MutableComponent output = Component.empty();
        List<ChatFormatting> formats = new ArrayList<>();
        for(String s : split) {
            if(s.isEmpty()) continue;
            char code = s.charAt(0);
            ChatFormatting format = ChatFormatting.getByCode(code);
            if(format != null) formats.add(format);

            if(s.length() > 1) {
                Style style = Style.EMPTY;
                MutableComponent text = Component.literal(s.substring(1));
                for (ChatFormatting f : formats) {
                    style = style.applyFormat(f);
                }
                text.setStyle(style);
                output.append(text);
                formats.clear();
            }
        }
        return output;
    }

    @Override
    public String ticksToTime(long ticks) {
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

    @Override
    public void staffChat(String message, UUID sender) {
        StringBuilder format = new StringBuilder("&r");
        for(char c : FeatureConfig.sc_format.get().toCharArray()) {
            format.append("&").append(c);
        }
        message = message.replace("&r", format);
        String text = formatString(message);
        Main.serverData.staffList.forEach(uuid -> {
            ServerPlayer staff = Methods.getPlayerFromUUID(uuid);
            staff.sendSystemMessage(Component.literal(text));
        });
    }

    @Override
    public void sendToChat(String message){
        sendToChat(Component.literal(formatString(message)));
    }

    @Override
    public void sendToChat(Component message){
        Main.mcServer.getPlayerList().broadcastSystemMessage(message,false);
        Main.bot.sendChatMessage(message.getString());
    }

    @Override
    public void sendToStaff(CommandSourceStack source, Component message) {
        sendToStaff(source, message.getString());
    }

    @Override
    public void sendToStaff(String name, UUID uuid, String message) {
        ServerPlayer player = Methods.getPlayerFromUUID(uuid);

        String tag = FeatureConfig.sc_tag.get();
        IPlayerData data = Main.database.get(uuid);
        if(data.isJailed()) tag = FeatureConfig.sc_jail_tag.get();
        staffChat(tag + "&r " + name + "&r: " + message, uuid);
        if(!uuid.equals(Main.bot.botUUID)) Main.bot.sendStaffMessage(player, message);
    }

    @Override
    public void sendToStaff(UUID uuid, String message) {
        sendToStaff(Methods.getDisplayName(uuid), uuid, message);

    }

    @Override
    public void sendToStaff(CommandSourceStack source, String message) {
        UUID uuid;
        try {
            uuid = source.getPlayerOrException().getUUID();
        } catch (CommandSyntaxException e) {
            uuid = UUID.randomUUID();
        }
        sendToStaff(uuid, message);
    }

    @Override
    public String listToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for(String s : list) {
            if(builder.toString().isEmpty()) builder.append(s);
            else builder.append(", ").append(s);
        }
        return builder.toString();
    }

    @Override
    public Component commandHelp(String[] content) {
        MutableComponent text = Component.literal(formatString(FeatureConfig.command_help_spacer.get() + "\n"));
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

    @Override
    public Component formatChat(UUID uuid, Component msg) {
        return formatChat(uuid, msg.getString());
    }

    @Override
    public Component formatChat(UUID uuid, String msg){
        MutableComponent text = (MutableComponent)formatUsername(uuid);
        text.append(Main.textUtils.formatString(": "));
        if (FeatureConfig.color_chat_enable.get()) {
            if (IElrolAPI.getInstance().getPermissionHandler().hasPermission(uuid, FeatureConfig.color_chat_perm.get())) {
                Logger.debug("Color chat enabled");
                text.append(Main.textUtils.format(msg));
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

    @Override
    public Component formatUsername(UUID uuid) {
        IPlayerData data = Main.database.get(uuid);
        MutableComponent text = Component.literal("");
        StringBuilder string = new StringBuilder();
        if (data.isPatreon()) string.append("&5[&dPatreon&5]&r");
        if (!data.getPrefix().isEmpty()) {
            String p = data.getPrefix();
            string.append(p);
            String s = p.substring(p.length() - 2);
            if(!s.startsWith("&")) string.append("&r ");
        }
        if (!data.getTitle().isEmpty()) {
            string.append(data.getTitle()).append("&r ");
        }
        string.append(data.getDisplayName());
        if (!data.getSuffix().isEmpty()) {
            string.append(" ").append(data.getSuffix());
        } else {
            string.append("&r");
        }
        text.append(formatString(string.toString()));
        return text;
    }

    @Override
    public void msgNoTag(ServerPlayer player, Component message) {
        player.sendSystemMessage(message);
    }

    @Override
    public void action(ServerPlayer player, Component message) {
        player.sendSystemMessage(message, true);
    }

    @Override
    public void msg(CommandContext<CommandSourceStack> context, Component translation) {
        Main.textUtils.msg(context.getSource(), translation);
    }

    @Override
    public void msg(ServerPlayer player, Component translation) {
        Main.textUtils.msg(player.createCommandSourceStack(), translation);
    }

    @Override
    public void msg(CommandSourceStack source, String text) {
        source.sendSuccess(Component.literal(ModInfo.getTag() + text), false);
    }

    @Override
    public void msgNoTag(CommandSourceStack source, Component text){
        source.sendSuccess(text, false);
    }

    @Override
    public void msg(CommandSourceStack source, Component translation) {
        source.sendSuccess(Component.literal(ModInfo.getTag()).append(translation), false);
    }

    @Override
    public void err(CommandContext<CommandSourceStack> context, Component translation) {
        Main.textUtils.err(context.getSource(), translation);
    }

    @Override
    public void err(ServerPlayer player, Component translation) {
        Main.textUtils.err(player.createCommandSourceStack(), translation);
    }

    @Override
    public void err(CommandSourceStack source, String text) {
        source.sendFailure(Component.literal(ModInfo.getTag() + ChatFormatting.RED + text));
    }

    @Override
    public void err(CommandSourceStack source, Component translation) {
        source.sendFailure(Component.literal(ModInfo.getTag() + ChatFormatting.RED).append(translation));
    }

    @Override
    public void tab_msg(CommandSourceStack source, String string) {
        String tab = "    ";
        source.sendFailure(Component.literal(tab + string));
    }

    @Override
    public void msg(CommandSourceStack source, String[] stringArray) {
        for (int i = 0; i < stringArray.length; ++i) {
            if (i == 0) {
                source.sendFailure(Component.literal(ModInfo.getTag() + stringArray[0]));
                continue;
            }
            Main.textUtils.tab_msg(source, stringArray[i]);
        }
    }

    @Override
    public String holiday(String string) {
        int month = Calendar.getInstance().get(2);
        Main.getLogger().info("The month is: " + month);
        return switch (month) {
            case 0 -> CustTextFormatting.jan(string);
            case 1 -> CustTextFormatting.feb(string);
            case 2 -> CustTextFormatting.mar(string);
            case 3 -> CustTextFormatting.apr(string);
            case 4 -> CustTextFormatting.may(string);
            case 5 -> CustTextFormatting.jun(string);
            case 6 -> CustTextFormatting.jul(string);
            case 7 -> CustTextFormatting.aug(string);
            case 8 -> CustTextFormatting.sep(string);
            case 9 -> CustTextFormatting.oct(string);
            case 10 -> CustTextFormatting.nov(string);
            case 11 -> CustTextFormatting.dec(string);
            default -> string;
        };
    }

    @Override
    public String format(String string){
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


    @Override
    public String formatString(String string) {
        if(string == null) return "";
        string = string.replace("\\n", "\n");
        StringBuilder text = new StringBuilder();
        Map<Character, ChatFormatting> colors = getColors();
        if(!string.contains("&")) return string;
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
                split = Main.textUtils.holiday(split.substring(1));
            } else {
                split = "&" + split;
            }
            text.append(split);
        }
        return text.toString();
    }

    @Override
    public void sendMessage(CommandSourceStack source, ServerPlayer player, String msg) {
        String message = ChatFormatting.DARK_GRAY + "[" + ChatFormatting.GRAY;
        IPlayerData pData = Main.database.get(player.getUUID());
        UUID uuid = UUID.randomUUID();
        try {
            ServerPlayer p = source.getPlayerOrException();
            uuid = p.getUUID();
            IPlayerData sData = Main.database.get(p.getUUID());
            sData.setLastMsg(player.getUUID());
            pData.setLastMsg(p.getUUID());
            Logger.debug("Source was a player");
        } catch (CommandSyntaxException e) {
            Logger.debug("Source was not player");
        }
        message += Methods.getDisplayName(source);
        message += ChatFormatting.DARK_GRAY + " >> " + ChatFormatting.GRAY;
        message += Methods.getDisplayName(player.createCommandSourceStack());
        message += ChatFormatting.DARK_GRAY + "] " + ChatFormatting.GRAY;
        message = FeatureConfig.color_chat_enable.get() && IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.createCommandSourceStack(), FeatureConfig.color_chat_perm.get()) ? message + Main.textUtils.formatString(msg) : message + msg;
        player.sendSystemMessage(Component.literal(message));
        source.sendSuccess(Component.literal(message), false);
    }

    @Override
    public Map<Character, ChatFormatting> getColors() {
        Map<Character, ChatFormatting> map = new HashMap<>();

        map.put('0', ChatFormatting.BLACK);
        map.put('1', ChatFormatting.DARK_BLUE);
        map.put('2', ChatFormatting.DARK_GREEN);
        map.put('3', ChatFormatting.DARK_AQUA);
        map.put('4', ChatFormatting.DARK_RED);
        map.put('5', ChatFormatting.DARK_PURPLE);
        map.put('6', ChatFormatting.GOLD);
        map.put('7', ChatFormatting.GRAY);
        map.put('8', ChatFormatting.DARK_GRAY);
        map.put('9', ChatFormatting.BLUE);
        map.put('a', ChatFormatting.GREEN);
        map.put('b', ChatFormatting.AQUA);
        map.put('c', ChatFormatting.RED);
        map.put('d', ChatFormatting.LIGHT_PURPLE);
        map.put('e', ChatFormatting.YELLOW);
        map.put('f', ChatFormatting.WHITE);

        map.put('k', ChatFormatting.OBFUSCATED);
        map.put('l', ChatFormatting.BOLD);
        map.put('m', ChatFormatting.STRIKETHROUGH);
        map.put('n', ChatFormatting.UNDERLINE);
        map.put('o', ChatFormatting.ITALIC);
        map.put('r', ChatFormatting.RESET);

        return map;
    }

    @Override
    public String stringToGolden(String parString, int parShineLocation, boolean parReturnToBlack){
        int stringLength = parString.length();
        if (stringLength < 1){
            return "";
        }
        StringBuilder outputString = new StringBuilder();
        for (int i = 0; i < stringLength; i++){
            if ((i+parShineLocation + Main.mcServer.getNextTickTime()/20)%88==0){
                outputString.append(ChatFormatting.WHITE).append(parString, i, i + 1);
            } else if ((i+parShineLocation + Main.mcServer.getNextTickTime()/20)%88==1){
                outputString.append(ChatFormatting.YELLOW).append(parString, i, i + 1);
            } else if ((i + parShineLocation+ Main.mcServer.getNextTickTime()/20)%88==87){
                outputString.append(ChatFormatting.YELLOW).append(parString, i, i + 1);
            } else {
                outputString.append(ChatFormatting.GOLD).append(parString, i, i + 1);
            }
        }
        if (parReturnToBlack){
            return outputString.toString() +ChatFormatting.BLACK;
        }
        return outputString.toString() + ChatFormatting.WHITE;
}

    /***
     *
     * @param amount The amount of currency to format
     * @param symbol If true, parses using symbol, otherwise uses singular/plural
     * @return The amount formatted to be readable
     */
    @Override
    public String parseCurrency(double amount, boolean symbol) {
        String formatted = String.format("%.2f", amount);

        if(!symbol){
            if(amount == 1) return formatted + " " + FeatureConfig.currency_singular.get();
            return formatted + " " + FeatureConfig.currency_plural.get();
        }
        return formatted + FeatureConfig.currency_symbol.get();
    }

    @Override
    public float parseCurrency(String string) {
        String symbol = FeatureConfig.currency_symbol.get();
        if(string.startsWith(symbol)) {
            string = string.substring(symbol.length());
        }
        if(string.isEmpty()) return 0F;
        return Float.parseFloat(string);
    }

    @Override
    public String stripFormatting(String string) {
        StringBuilder stripped = new StringBuilder();
        String[] split = string.split("[&§]");
        for(int i = 0; i < split.length; i++) {
            String s = split[i];
            if(i > 0) s = s.substring(1);
            stripped.append(s);
        }
        return stripped.toString();
    }

    @Override
    public void sendConfirmation(ServerPlayer player, Component[] lines) {
        UUID uuid = player.getUUID();
        Component spacer = Component.literal(ChatFormatting.AQUA + String.join("", Collections.nCopies(45, "#")));
        player.sendSystemMessage(spacer);
        for(Component line : lines) player.sendSystemMessage(line);
        player.sendSystemMessage(spacer);
    }

    @Override
    public String generateString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}

