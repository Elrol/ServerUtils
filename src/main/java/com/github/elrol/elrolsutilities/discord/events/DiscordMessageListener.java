package com.github.elrol.elrolsutilities.discord.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.discord.DiscordBot;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;


public class DiscordMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        DiscordBot bot = Main.bot;

        long userID = event.getAuthor().getIdLong();
        IPlayerData data = IElrolAPI.getInstance().getPlayerDatabase().get(userID);

        String displayName = data == null ? event.getAuthor().getName() : data.getDisplayName();
        String sender = data == null ? event.getAuthor().getName() : bot.getName(data.getUUID());
        String content = event.getMessage().getContentDisplay();

        TextComponent text;
        if(data != null) {
            text = TextUtils.formatChat(data.getUUID(),content);
        } else {
            String tag = Main.bot.getDiscordTag();
            String msg = (tag.isEmpty() ? "" : tag + "&r ") + event.getAuthor().getName() + ": " + content;
            text = new TextComponent(TextUtils.formatString(msg));
        }

        long id = event.getMessage().getGuildChannel().getIdLong();

        if(bot.isChatChannel(id)) {
            bot.chatChannels.forEach(c -> {
                if(c.getIdLong() == id) return;
                c.sendMessage(new MessageBuilder(TextUtils.stripFormatting("[Discord] " + sender + ": " + content)).build()).queue();
            });
            Main.mcServer.getPlayerList().broadcastMessage(text, ChatType.CHAT, bot.botUUID);
        } else if(bot.isInfoChannel(id)) {
            bot.infoChannels.forEach(c -> {
                if(c.getIdLong() == id) return;
                c.sendMessage(new MessageBuilder(TextUtils.stripFormatting("[Discord] " + sender + ": " + content)).build()).queue();
            });
            Main.mcServer.getPlayerList().broadcastMessage(text, ChatType.CHAT, bot.botUUID);
        } else if(bot.isStaffChannel(id)) {
            bot.staffChannels.forEach(c -> {
                if(c.getIdLong() == id) return;
                c.sendMessage(new MessageBuilder(TextUtils.stripFormatting("[Discord] " + sender + ": " + content)).build()).queue();
            });
            TextUtils.sendToStaff(displayName, bot.botUUID, content);
        }
        super.onMessageReceived(event);
    }
}
