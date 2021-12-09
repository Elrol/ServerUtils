package com.github.elrol.elrolsutilities.discord.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.DiscordConfig;
import com.github.elrol.elrolsutilities.discord.DiscordBot;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class DiscordMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        //Main.mcServer.getPlayerList().broadcastMessage(TextUtils.formatChat(player,cmd.substring(2)), ChatType.CHAT, player.getUUID())
        String tag = DiscordConfig.discordTag.get();
        String content = event.getMessage().getContentDisplay();
        String msg = (tag.isEmpty() ? "" : tag + "&r ") + event.getAuthor().getName() + ": " + content;
        StringTextComponent text = new StringTextComponent(TextUtils.formatString(msg));

        long id = event.getMessage().getGuildChannel().getIdLong();
        DiscordBot bot = Main.bot;

        if(bot.chatChannel.getIdLong() == id || bot.infoChannel.getIdLong() == id) {
            Main.mcServer.getPlayerList().broadcastMessage(text, ChatType.CHAT, bot.botUUID);
        } else if(bot.staffChannel.getIdLong() == id) {
            TextUtils.sendToStaff(event.getAuthor().getName(), bot.botUUID, content);
        } else if(bot.consoleChannel.getIdLong() == id) {
            if(content.startsWith("!run ")) {
                CommandSource console = Main.mcServer.createCommandSourceStack();
                Main.mcServer.getCommands().performCommand(console, content.substring(5));
            }
        }
        super.onMessageReceived(event);
    }
}
