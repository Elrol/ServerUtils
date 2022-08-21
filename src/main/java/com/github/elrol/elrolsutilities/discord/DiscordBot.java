package com.github.elrol.elrolsutilities.discord;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.discord.events.DiscordMessageListener;
import com.github.elrol.elrolsutilities.discord.init.SlashCommands;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.minecraft.entity.player.ServerPlayerEntity;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscordBot {

    private String token = "";
    private String botID = "";

    private String discordTag = "&8[&eDiscord&8]";
    private String serverName = "";

    private transient boolean enabled;
    private boolean nicks = true;
    private boolean titles = true;
    private boolean tags = true;

    public List<DiscordServerInfo> guildInfo = new ArrayList<>();

    public transient UUID botUUID;
    public transient JDA bot;
    private final transient List<Guild> guilds = new ArrayList<>();

    public transient List<TextChannel> chatChannels = new ArrayList<>();
    public transient List<TextChannel> staffChannels = new ArrayList<>();
    public transient List<TextChannel> infoChannels = new ArrayList<>();
    public transient List<TextChannel> consoleChannels = new ArrayList<>();


    private transient final DiscordMessageListener listener = new DiscordMessageListener();
    private transient final SlashCommands commands = new SlashCommands();

    public DiscordBot() {
        guildInfo.add(new DiscordServerInfo());
        save();
    }

    public void init() {
        enabled = FeatureConfig.discord_bot_enable.get();

        if(!enabled) {
            Logger.log("Discord bot not enabled");
            return;
        }
        if(guildInfo.isEmpty()) {
            guildInfo.add(new DiscordServerInfo());
        }
        if(botID.isEmpty()) {
            botUUID = UUID.randomUUID();
            botID = botUUID.toString();
        } else {
            botUUID = UUID.fromString(botID);
        }
        if(token.isEmpty()) {
            Logger.err("Discord bot token is empty");
            return;
        }
        JDABuilder jda = JDABuilder.createDefault(token);
        jda.setActivity(Activity.playing(serverName.isEmpty() ? "Server Utils" : serverName));
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
        save();
    }

    public void shutdown() {
        if(bot != null)
            bot.shutdown();
    }

    public String getName(ServerPlayerEntity player) {
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

    public void sendChatMessage(ServerPlayerEntity player, String message) {
        if(enabled && isOnline()) {
            Message msg = new MessageBuilder(TextUtils.stripFormatting(getName(player) + message)).build();
            chatChannels.forEach(c -> c.sendMessage(msg).queue());
        }
    }

    public void sendStaffMessage(ServerPlayerEntity player, String message) {
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

    public void sendConsoleMessage(ServerPlayerEntity player, String message) {
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

    public String getDiscordTag() { return discordTag; }

    public void update() {
        int players = Main.mcServer.getPlayerList().getPlayerCount();
        Logger.log("Current players: " + players);
        if(players > 0) {
            bot.getPresence().setActivity(Activity.playing(" currently: " + players));
        }
    }

    public boolean isOnline() {
        if(bot == null) return false;
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

    public static DiscordBot load() {
        return JsonMethod.load(ModInfo.Constants.configdir, "DiscordSettings.json", DiscordBot.class);
    }

    public void save() {
        JsonMethod.save(ModInfo.Constants.configdir, "DiscordSettings.json", this);
    }

    public static class DiscordServerInfo {
        public final Long guildID;
        public final Long chatID;
        public final Long staffID;
        public final Long infoID;
        public final Long consoleID;

        public DiscordServerInfo() {
            guildID = 0L;
            chatID = 0L;
            staffID = 0L;
            infoID = 0L;
            consoleID = 0L;
        }

        public DiscordServerInfo(Long gID, Long cID, Long sID, Long iID, Long conID) {
            guildID = gID;
            chatID = cID;
            staffID = sID;
            infoID = iID;
            consoleID = conID;
        }
    }
}
