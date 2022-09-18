package dev.elrol.serverutilities.libs.adapters;

import dev.elrol.serverutilities.discord.DiscordBot;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DiscordServerInfoAdapter extends TypeAdapter<DiscordBot.DiscordServerInfo> {

    @Override
    public void write(JsonWriter out, DiscordBot.DiscordServerInfo value) throws IOException {
        out.beginObject();

        out.name("guild");
        out.value(value.guildID);

        out.name("chat");
        out.value(value.chatID);

        out.name("staff");
        out.value(value.staffID);

        out.name("info");
        out.value(value.infoID);

        out.name("console");
        out.value(value.consoleID);

        out.endObject();
    }

    @Override
    public DiscordBot.DiscordServerInfo read(JsonReader in) throws IOException {
        in.beginObject();
        in.nextName();
        long guild = in.nextLong();
        in.nextName();
        long chat = in.nextLong();
        in.nextName();
        long staff = in.nextLong();
        in.nextName();
        long info = in.nextLong();
        in.nextName();
        long console = in.nextLong();
        in.endObject();

        return new DiscordBot.DiscordServerInfo(guild, chat, staff, info, console);
    }
}
