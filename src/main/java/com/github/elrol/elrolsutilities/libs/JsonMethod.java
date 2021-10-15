package com.github.elrol.elrolsutilities.libs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import com.github.elrol.elrolsutilities.libs.adapters.InterfaceAdapter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.nbt.CompoundNBT;

public class JsonMethod {
    public static void save(File file, String name, Object obj) {
        if (file.mkdirs()) {
            Logger.log(file.getAbsolutePath() + " was missing and has been created.");
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CompoundNBT.class, new InterfaceAdapter())
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
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
        Gson gson = new GsonBuilder().registerTypeAdapter(CompoundNBT.class, new InterfaceAdapter()).create();
        if (f.exists()) {
            if (f.setWritable(true)) {
                Logger.err("File was set to be writeable");
            }
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
                return gson.fromJson(bufferedReader, clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

