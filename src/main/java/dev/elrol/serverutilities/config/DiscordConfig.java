package dev.elrol.serverutilities.config;

import dev.elrol.serverutilities.discord.DiscordBot;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber
public class DiscordConfig {

    public static ForgeConfigSpec.ConfigValue<String> token;
    public static ForgeConfigSpec.ConfigValue<String> botUUID;
    public static ForgeConfigSpec.ConfigValue<String> serverName;

    public static ForgeConfigSpec.BooleanValue showRank;
    public static ForgeConfigSpec.BooleanValue showTitles;
    public static ForgeConfigSpec.BooleanValue showNicknames;
    public static ForgeConfigSpec.ConfigValue<String> discordTag;

    public static ForgeConfigSpec.ConfigValue<List<DiscordBot.DiscordServerInfo>> discordInfo;

    public static void init(ForgeConfigSpec.Builder server) {
        server.comment("The token for the bot");
        token = server.define("token", "");

        server.comment("The UUID for the bot. If left empty, it will be generated");
        botUUID = server.define("uuid", "");

        server.comment("The server's name");
        serverName = server.define("server name", "");

        server.push("formatting");
            showRank = server.define("show rank", true);
            showTitles = server.define("show titles", true);
            showNicknames = server.define("show nicknames", true);
            discordTag = server.define("discord tag", "&8[&eDiscord&8]");
        server.pop();
        server.push("Server info");
            discordInfo = server.define("server info", Collections.singletonList(new DiscordBot.DiscordServerInfo()));
        server.pop();
    }



}
