package com.github.elrol.elrolsutilities.discord;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.DiscordConfig;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.discord.events.DiscordMessageListener;
import com.github.elrol.elrolsutilities.discord.init.SlashCommands;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.minecraft.server.level.ServerPlayer;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscordBot {

    public UUID botUUID;

    private boolean enabled;
    private boolean nicks;
    private boolean titles;
    private boolean tags;

    public JDA bot;
    private List<Guild> guilds = new ArrayList<>();

    public List<TextChannel> chatChannels = new ArrayList<>();
    public List<TextChannel> staffChannels = new ArrayList<>();
    public List<TextChannel> infoChannels = new ArrayList<>();
    public List<TextChannel> consoleChannels = new ArrayList<>();

    public List<DiscordServerInfo> guildInfo;

    private final DiscordMessageListener listener = new DiscordMessageListener();
    private final SlashCommands commands = new SlashCommands();

    public void init() {
        enabled = FeatureConfig.discord_bot_enable.get();

        if(!enabled) {
            Logger.log("Discord bot not enabled");
            return;
        }
        guildInfo = DiscordConfig.discordInfo.get();
        String id = DiscordConfig.botUUID.get();
        if(id.isEmpty()) {
            botUUID = UUID.randomUUID();
            DiscordConfig.botUUID.set(botUUID.toString());
        } else {
            botUUID = UUID.fromString(id);
        }

        nicks = DiscordConfig.showNicknames.get();
        titles = DiscordConfig.showTitles.get();
        tags = DiscordConfig.showRank.get();

        String token = DiscordConfig.token.get();
        if(token.isEmpty()) {
            Logger.err("Discord bot token is empty");
            return;
        }
        JDABuilder jda = JDABuilder.createDefault(token);

        String name = DiscordConfig.serverName.get();
        jda.setActivity(Activity.playing(name.isEmpty() ? "Server Utils" : name));
        jda.setStatus(OnlineStatus.ONLINE);

        jda.addEventListeners(listener);
        jda.addEventListeners(commands);

        try {
            bot = jda.build().awaitReady();

            if(guildInfo != null) {
                for(DiscordServerInfo info : guildInfo) {
                    if(info.guildID > 0L) {
                        Guild guild = bot.getGuildById(info.guildID);
                        if(guild == null) {
                            Main.getLogger().error("Discord Guild ID [" + info.guildID + "] was invalid.");
                            continue;
                        }
                        guilds.add(guild);
                        commands.init(guild);
                        if(info.chatID > 0L) {
                            TextChannel channel = bot.getTextChannelById(info.chatID);
                            if(channel != null) chatChannels.add(channel);
                        }
                        if(info.infoID > 0L) {
                            TextChannel channel = bot.getTextChannelById(info.infoID);
                            if(channel != null) infoChannels.add(channel);
                        }
                        if(info.staffID > 0L) {
                            TextChannel channel = bot.getTextChannelById(info.staffID);
                            if(channel != null) staffChannels.add(channel);
                        }
                        if(info.consoleID > 0L) {
                            TextChannel channel = bot.getTextChannelById(info.consoleID);
                            if(channel != null) consoleChannels.add(channel);
                        }
                    }
                }
            }
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        if(bot != null)
            bot.shutdown();
    }

    public String getName(ServerPlayer player) {
        if(player == null) return "NULL";
        return getName(player.getUUID());
    }

    public String getName(UUID uuid) {
        if(uuid == null) return "NULL";
        String name = "";
        IPlayerData data = Main.database.get(uuid);
        if(tags) name += data.getPrefix() + " ";
        if(titles) name += data.getTitle() + " ";
        if(nicks) name += data.getDisplayName();
        name += ": ";
        return name;
    }

    public void sendChatMessage(ServerPlayer player, String message) {
        if(enabled && isOnline()) {
            Message msg = new MessageBuilder(TextUtils.stripFormatting(getName(player) + message)).build();
            chatChannels.forEach(c -> c.sendMessage(msg).queue());
        }
    }

    public void sendStaffMessage(ServerPlayer player, String message) {
        if(enabled && isOnline()) {
            Message msg = new MessageBuilder(TextUtils.stripFormatting(getName(player) + message)).build();
            staffChannels.forEach(c -> c.sendMessage(msg).queue());
        }
    }

    public void sendInfoMessage(String message) {
        if(enabled && isOnline()) {
            Message msg = new MessageBuilder(TextUtils.stripFormatting(message)).build();
            infoChannels.forEach(c -> c.sendMessage(msg).queue());
        }
    }

    public void sendConsoleMessage(ServerPlayer player, String message) {
        if(enabled && isOnline()) {
            Message msg = new MessageBuilder(TextUtils.stripFormatting(getName(player) + message)).build();
            consoleChannels.forEach(c -> c.sendMessage(msg).queue());
        }
    }

    public String getDiscordName(long id) {
        for(Guild guild : guilds) {
            Member member = guild.getMemberById(id);
            if(member != null) {
                return member.getUser().getName();
            }
        }
        return "NULL";
    }

    public void update() {
        int players = Main.mcServer.getPlayerList().getPlayerCount();
        Logger.log("Current players: " + players);
        if(players > 0) {
            bot.getPresence().setActivity(Activity.playing(" currently: " + players));
        }
    }

    public boolean isOnline() {
        JDA.Status status = bot.getStatus();
        return !(status.equals(JDA.Status.SHUTDOWN) || status.equals(JDA.Status.SHUTTING_DOWN));
    }

    public boolean isChatChannel(Long id) {
        for(TextChannel channel : chatChannels) {
            if(channel.getIdLong() == id) return true;
        }
        return false;
    }

    public boolean isInfoChannel(Long id) {
        for(TextChannel channel : chatChannels) {
            if(channel.getIdLong() == id) return true;
        }
        return false;
    }

    public boolean isStaffChannel(Long id) {
        for(TextChannel channel : chatChannels) {
            if(channel.getIdLong() == id) return true;
        }
        return false;
    }

    public boolean isConsoleChannel(Long id) {
        for(TextChannel channel : chatChannels) {
            if(channel.getIdLong() == id) return true;
        }
        return false;
    }

    public static class DiscordServerInfo {
        Long guildID = 0L;
        Long chatID = 0L;
        Long staffID = 0L;
        Long infoID = 0L;
        Long consoleID = 0L;

        public DiscordServerInfo() {}
        public DiscordServerInfo(Long gID, Long cID, Long sID, Long iID, Long conID) {
            guildID = gID;
            chatID = cID;
            staffID = sID;
            infoID = iID;
            consoleID = conID;
        }
    }
}
