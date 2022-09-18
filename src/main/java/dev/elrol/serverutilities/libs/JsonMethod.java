package dev.elrol.serverutilities.libs;

import dev.elrol.serverutilities.discord.DiscordBot;
import dev.elrol.serverutilities.libs.adapters.DiscordServerInfoAdapter;
import dev.elrol.serverutilities.libs.adapters.GsonLocalDateTime;
import dev.elrol.serverutilities.libs.adapters.InterfaceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.CompoundTag;

import java.io.*;
import java.time.LocalDateTime;

public class JsonMethod {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(CompoundTag.class, new InterfaceAdapter())
            .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTime())
            .registerTypeAdapter(DiscordBot.DiscordServerInfo.class, new DiscordServerInfoAdapter())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static void save(File file, String name, Object obj) {
        if (file.mkdirs()) {
            Logger.log(file.getAbsolutePath() + " was missing and has been created.");
        }

        try {
            String jsonString = gson.toJson(obj);
            Logger.debug("Saving file to " + file.getAbsolutePath());
            FileWriter writer = new FileWriter(new File(file, name));
            writer.write(jsonString);
            writer.close();
        }
        catch (IOException e) {
            System.out.println("exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static <T> T load(File file, String name, Class<T> clazz) {
        if (file.mkdirs()) {
            Logger.log(file.getAbsolutePath() + " was missing and has been created.");
        }
        File f = new File(file, name);
        Logger.log("Data file: " + f.getAbsolutePath());
        if (f.exists()) {
            if (f.setWritable(true)) {
                //Logger.err("File was set to be writeable");
            }
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
                return gson.fromJson(bufferedReader, clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void newSave(File file, String name, Object obj) {
        if(file.mkdirs()) Logger.log("Directory created at: " + file.getAbsolutePath());

        try(FileWriter writer = new FileWriter(new File(file, name))) {
            gson.toJson(obj, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T newLoad(File file, String name, Class<T> clazz) {
        if(file.mkdirs()) Logger.log("Directory created at: " + file.getAbsolutePath());

        File f = new File(file, name);
        if(f.exists()) {
            try(FileReader reader = new FileReader(f)) {
                return gson.fromJson(reader, clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T newLoad(File file, String name, TypeToken<T> type) {
        if(file.mkdirs()) Logger.log("Directory created at: " + file.getAbsolutePath());

        File f = new File(file, name);
        if(f.exists()) {
            try(FileReader reader = new FileReader(f)) {
                return gson.fromJson(reader, type.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

