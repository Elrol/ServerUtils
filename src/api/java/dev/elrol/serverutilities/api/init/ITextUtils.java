package dev.elrol.serverutilities.api.init;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ITextUtils {

    Component formatComponent(String input);

    String ticksToTime(long ticks);

    void staffChat(String message, UUID sender);

    void sendToChat(String message);

    void sendToChat(Component message);

    void sendToStaff(CommandSourceStack source, Component message);

    void sendToStaff(String name, UUID uuid, String message);

    void sendToStaff(UUID uuid, String message);

    void sendToStaff(CommandSourceStack source, String message);

    String listToString(List<String> list);

    Component commandHelp(String[] content);

    Component formatChat(UUID uuid, Component msg);

    Component formatChat(UUID uuid, String msg);

    Component formatUsername(UUID uuid);

    void msgNoTag(ServerPlayer player, Component message);

    void action(ServerPlayer player, Component message);

    void msg(CommandContext<CommandSourceStack> context, Component translation);

    void msg(ServerPlayer player, Component translation);

    void msg(CommandSourceStack source, String text);

    void msgNoTag(CommandSourceStack source, Component text);

    void msg(CommandSourceStack source, Component translation);

    void err(CommandContext<CommandSourceStack> context, Component translation);

    void err(ServerPlayer player, Component translation);

    void err(CommandSourceStack source, String text);

    void err(CommandSourceStack source, Component translation);

    void tab_msg(CommandSourceStack source, String string);

    void msg(CommandSourceStack source, String[] stringArray);

    String holiday(String string);

    String format(String string);

    String formatString(String string);

    void sendMessage(CommandSourceStack source, ServerPlayer player, String msg);

    Map<Character, ChatFormatting> getColors();

    String stringToGolden(String parString, int parShineLocation, boolean parReturnToBlack);

    String parseCurrency(double amount, boolean symbol);

    float parseCurrency(String string);

    String stripFormatting(String string);

    void sendConfirmation(ServerPlayer player, Component[] lines);

    String generateString();
}
