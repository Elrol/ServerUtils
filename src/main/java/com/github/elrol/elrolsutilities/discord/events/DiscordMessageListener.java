package com.github.elrol.elrolsutilities.discord.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.DiscordConfig;
import com.github.elrol.elrolsutilities.discord.DiscordBot;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;


public class DiscordMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        long userID = event.getAuthor().getIdLong();
        IPlayerData data = IElrolAPI.getInstance().getPlayerDatabase().get(userID);

        String content = event.getMessage().getContentDisplay();

        StringTextComponent text;
        if(data != null) {
            text = TextUtils.formatChat(data.getUUID(),content);
        } else {
            String tag = DiscordConfig.discordTag.get();
            String msg = (tag.isEmpty() ? "" : tag + "&r ") + event.getAuthor().getName() + ": " + content;
            text = new StringTextComponent(TextUtils.formatString(msg));
        }

        long id = event.getMessage().getGuildChannel().getIdLong();
        DiscordBot bot = Main.bot;

        if(bot.chatChannel.getIdLong() == id || bot.infoChannel.getIdLong() == id) {
            Main.mcServer.getPlayerList().broadcastMessage(text, ChatType.CHAT, bot.botUUID);
        } else if(bot.staffChannel.getIdLong() == id) {
            TextUtils.sendToStaff(event.getAuthor().getName(), bot.botUUID, content);
        }
        super.onMessageReceived(event);
    }
}
