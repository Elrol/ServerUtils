package dev.elrol.serverutilities.data;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.Logger;
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
    boolean flag = false;
    public void init(){
        Logger.log("Starting loading patreon list.");
        if(flag){
            try(BufferedInputStream stream = new BufferedInputStream(new URL("https://www.pastebin.com/raw/vvAFUqWG").openStream())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                Gson gson = new Gson();
                PatreonList list = gson.fromJson(reader, PatreonList.class);
                if(list.patreons != null) patreons = list.patreons;
                Logger.log("Patreon list loaded");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Logger.log("Patreon list loading skipped");
        }

    }

    public boolean has(UUID uuid){
        Main.getLogger().info("Patreons: " + patreons);
        return patreons.contains(uuid);
    }
}
