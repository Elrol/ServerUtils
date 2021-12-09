package com.github.elrol.elrolsutilities.discord;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.DiscordConfig;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.discord.events.DiscordMessageListener;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.minecraft.entity.player.PlayerEntity;

import javax.security.auth.login.LoginException;
import java.util.UUID;

public class DiscordBot {

    public UUID botUUID;

    private boolean enabled;
    private boolean nicks;
    private boolean tags;

    private JDABuilder jda;
    public JDA bot;
    private Guild guild;

    public TextChannel chatChannel;
    public TextChannel staffChannel;
    public TextChannel infoChannel;
    public TextChannel consoleChannel;

    private final DiscordMessageListener listener = new DiscordMessageListener();

    public void init() {
        enabled = FeatureConfig.discord_bot_enable.get();

        if(!enabled) {
            Logger.log("Discord bot not enabled");
            return;
        }
        String id = DiscordConfig.botUUID.get();
        if(id.isEmpty()) {
            botUUID = UUID.randomUUID();
            DiscordConfig.botUUID.set(botUUID.toString());
        } else {
            botUUID = UUID.fromString(id);
        }

        nicks = DiscordConfig.showNicknames.get();
        tags = DiscordConfig.showRank.get();

        String token = DiscordConfig.token.get();
        if(token.isEmpty()) {
            Logger.err("Discord bot token is empty");
            return;
        }
        jda = JDABuilder.createDefault(token);

        String name = DiscordConfig.serverName.get();
        jda.setActivity(Activity.playing(name.isEmpty() ? "Server Utils" : name));
        jda.setStatus(OnlineStatus.ONLINE);

        jda.addEventListeners(listener);

        try {
            bot = jda.build().awaitReady();
            Long guildID = DiscordConfig.guildID.get();
            guild = bot.getGuildById(guildID);
            if(guild == null) {
                Logger.err("Guild ID was invalid");
                bot.getGuilds().forEach(g -> Logger.err(g.getName() + ": " + g.getIdLong()));
            }

            chatChannel = bot.getTextChannelById(DiscordConfig.chatChannelID.get());
            if(chatChannel == null) Logger.err("Chat Channel ID was invalid");

            staffChannel = bot.getTextChannelById(DiscordConfig.staffChannelID.get());
            if(staffChannel == null) Logger.err("Staff Channel ID was invalid");

            infoChannel = bot.getTextChannelById(DiscordConfig.infoChannelID.get());
            if(infoChannel == null) Logger.err("Info Channel ID was invalid");

            consoleChannel = bot.getTextChannelById(DiscordConfig.consoleChannelID.get());
            if(consoleChannel == null) Logger.err("Console Channel ID was invalid");

        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void shutdown() {

    }

    private String getName(PlayerEntity player) {
        String name = "";
        if(player != null) {
            IPlayerData data = Main.database.get(player.getUUID());
            if(tags) name += data.getPrefix() + " ";
            if(nicks) name += data.getDisplayName();
            else name += player.getName().getString();
            name += ": ";
        }
        return name;
    }

    public void sendChatMessage(PlayerEntity player, String message) {
        if(enabled && chatChannel != null) {
            MessageBuilder msg = new MessageBuilder(
                    TextUtils.stripFormatting(getName(player) + message));
            chatChannel.sendMessage(msg.build()).queue();
        }
    }

    public void sendStaffMessage(PlayerEntity player, String message) {
        if(enabled && staffChannel != null) {
            Message msg = new MessageBuilder(
                    TextUtils.stripFormatting(getName(player) + message)).build();
            staffChannel.sendMessage(msg).queue();
        }
    }

    public void sendInfoMessage(String message) {
        if(enabled && infoChannel != null) {
            Message msg = new MessageBuilder(
                    TextUtils.stripFormatting(message)).build();
            infoChannel.sendMessage(msg).queue();
        }
    }

    public void sendConsoleMessage(PlayerEntity player, String message) {
        if(enabled && consoleChannel != null) {
            Message msg = new MessageBuilder(
                    TextUtils.stripFormatting(getName(player) + message)).build();
            consoleChannel.sendMessage(msg).queue();
        }
    }
}
