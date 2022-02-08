package com.github.elrol.elrolsutilities.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DiscordConfig {

    public static ForgeConfigSpec.ConfigValue<String> token;
    public static ForgeConfigSpec.ConfigValue<String> botUUID;
    public static ForgeConfigSpec.ConfigValue<Long> guildID;
    public static ForgeConfigSpec.ConfigValue<Long> chatChannelID;
    public static ForgeConfigSpec.ConfigValue<Long> staffChannelID;
    public static ForgeConfigSpec.ConfigValue<Long> infoChannelID;
    public static ForgeConfigSpec.ConfigValue<Long> consoleChannelID;
    public static ForgeConfigSpec.ConfigValue<String> serverName;

    public static ForgeConfigSpec.BooleanValue showRank;
    public static ForgeConfigSpec.BooleanValue showNicknames;
    public static ForgeConfigSpec.ConfigValue<String> discordTag;

    public static void init(ForgeConfigSpec.Builder server) {
        server.comment("The token for the bot");
        token = server.define("token", "");

        server.comment("The UUID for the bot. If left empty, it will be generated");
        botUUID = server.define("uuid", "");

        server.comment("The discord server id");
        guildID = server.define("server id", 0L);

        server.comment("The channel id for where the chats should be");
        chatChannelID = server.define("chat channel", 0L);

        server.comment("The channel id for where the staff chats should be");
        staffChannelID = server.define("staff channel", 0L);

        server.comment("The channel id for where the info chats should be");
        infoChannelID = server.define("info channel", 0L);

        server.comment("The channel id for where commands will be recognized");
        consoleChannelID = server.define("command channel", 0L);

        server.comment("The server's name");
        serverName = server.define("server name", "");

        server.push("formatting");
            showRank = server.define("show rank", true);
            showNicknames = server.define("show nicknames", true);
            discordTag = server.define("discord tag", "&8[&eDiscord&8]");
        server.pop();
    }

}
