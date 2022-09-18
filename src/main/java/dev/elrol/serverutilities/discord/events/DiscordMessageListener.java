package dev.elrol.serverutilities.discord.events;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.discord.DiscordBot;
import dev.elrol.serverutilities.libs.text.TextUtils;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.network.chat.Component;
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

        Component text;
        if(data != null) {
            text = Main.textUtils.formatChat(data.getUUID(),content);
        } else {
            String tag = Main.bot.getDiscordTag();
            String msg = (tag.isEmpty() ? "" : tag + "&r ") + event.getAuthor().getName() + ": " + content;
            text = Component.literal(Main.textUtils.formatString(msg));
        }

        long id = event.getMessage().getGuildChannel().getIdLong();

        if(bot.isChatChannel(id)) {
            bot.chatChannels.forEach(c -> {
                if(c.getIdLong() == id) return;
                c.sendMessage(new MessageBuilder(Main.textUtils.stripFormatting("[Discord] " + sender + ": " + content)).build()).queue();
            });
            Main.mcServer.getPlayerList().broadcastSystemMessage(text,false);
        } else if(bot.isInfoChannel(id)) {
            bot.infoChannels.forEach(c -> {
                if(c.getIdLong() == id) return;
                c.sendMessage(new MessageBuilder(Main.textUtils.stripFormatting("[Discord] " + sender + ": " + content)).build()).queue();
            });
            Main.mcServer.getPlayerList().broadcastSystemMessage(text,false);
        } else if(bot.isStaffChannel(id)) {
            bot.staffChannels.forEach(c -> {
                if(c.getIdLong() == id) return;
                c.sendMessage(new MessageBuilder(Main.textUtils.stripFormatting("[Discord] " + sender + ": " + content)).build()).queue();
            });
            Main.textUtils.sendToStaff(displayName, bot.botUUID, content);
        }
        super.onMessageReceived(event);
    }
}
