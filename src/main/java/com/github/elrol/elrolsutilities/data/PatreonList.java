package com.github.elrol.elrolsutilities.data;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class PatreonList {

    public ArrayList<UUID> patreons = new ArrayList<>();

    public void init(){
        Logger.log("Starting loading patreon list.");
        try(BufferedInputStream stream = new BufferedInputStream(new URL("https://www.pastebin.com/raw/vvAFUqWG").openStream())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            Gson gson = new Gson();
            PatreonList list = gson.fromJson(reader, PatreonList.class);
            if(list.patreons != null) patreons = list.patreons;
            // Main.getLogger().info("Patreons: " + patreons);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.log("Patreon list loaded");
    }

    public boolean has(UUID uuid){
        Main.getLogger().info("Patreons: " + patreons);
        return patreons.contains(uuid);
    }
}
