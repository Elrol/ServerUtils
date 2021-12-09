package com.github.elrol.elrolsutilities.data;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.api.data.IPlayerDatabase;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDatabase implements IPlayerDatabase {
    Map<UUID, PlayerData> database = new HashMap<>();

    public PlayerDatabase() {
        loadAll();
    }

    @Override
    public boolean isPresent(UUID uuid) {
        return this.database.containsKey(uuid);
    }

    public Map<UUID, PlayerData> getDatabase() {
        return this.database;
    }

    @Override
    public IPlayerData get(UUID uuid) {
        if(uuid == null) return null;
        if (database.containsKey(uuid)) {
            Logger.debug("Data found. Loading.");
            return database.get(uuid);
        }
        PlayerData data = load(uuid);
        if (data == null) {
            Logger.debug("Data was null. Registering.");
            return registerPlayerData(uuid);
        }
        Logger.debug("Loading data from file.");
        return data;
    }

    private PlayerData registerPlayerData(UUID uuid) {
        PlayerData data = new PlayerData(uuid);
        data.update();
        this.database.put(uuid, data);
        this.save(uuid);
        return data;
    }

    public void set(PlayerData data) {
        UUID uuid = data.uuid;
        if (this.database.containsKey(uuid)) {
            Logger.err("Attempted to override PlayerDatabase");
            return;
        }
        this.database.put(uuid, data);
        this.save(uuid);
    }

    public void saveAll() {
        Logger.log("Saving all player data");
        for (UUID uuid : this.database.keySet()) {
            this.save(uuid);
        }
    }

    public void loadAll() {
        File file = new File(Main.dir, "/playerdata");
        File[] files = file.listFiles();
        if(files == null) return;
        for (File f : files) {
            if (!f.getName().endsWith(".pdat")) continue;
            String s = f.getName().replace(".pdat", "");
            UUID uuid = UUID.fromString(s);
            this.load(uuid);
            Logger.debug("Loading playerdata for: " + s);
        }
    }

    @Override
    public void save(UUID uuid) {
        JsonMethod.save(new File(Main.dir, "/playerdata"), "/" + uuid + ".pdat", this.get(uuid));
    }

    @Override
    public void save(IPlayerData data){
        JsonMethod.save(new File(Main.dir, "/playerdata"), "/" + data.getUUID() + ".pdat", data);
    }

    public PlayerData load(UUID uuid) {
        File file = new File(Main.dir, "/playerdata");
        PlayerData data = JsonMethod.load(file, "/" + uuid.toString() + ".pdat", PlayerData.class);
        if (data == null) {
            data = this.registerPlayerData(uuid);
            Logger.log("PlayerData not found for " + uuid + ". Creating new data.");
            return data;
        }
        data.isPatreon = Main.patreonList.has(uuid);
        database.put(uuid, data);
        data.update();
        return data;
    }

    public void remove(UUID uuid) {
        if (this.database.containsKey(uuid)) {
            this.database.remove(uuid);
            Logger.log("PlayerData for " + uuid.toString() + " has been unloaded");
        } else {
            Logger.err("PlayerData for " + uuid.toString() + " can not be found");
        }
    }

    public void clear(){
        database.clear();
    }
}

