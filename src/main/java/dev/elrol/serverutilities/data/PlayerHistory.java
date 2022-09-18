package dev.elrol.serverutilities.data;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Methods;

import java.io.File;
import java.util.*;

public class PlayerHistory {

    private Map<UUID, List<String>> warnings = new HashMap<>();
    private Map<UUID, List<String>> jails = new HashMap<>();
    private Map<UUID, List<String>> tempbans = new HashMap<>();
    private Map<UUID, List<String>> bans = new HashMap<>();

    public String[] lookUp(UUID uuid) {
        IPlayerData data = Main.database.get(uuid);
        return new String[] {
                "Username: " + data.getDisplayName(),
                "UUId:" + uuid,
                "First Join: " + data.getFirstJoin(),
                "Last Seen: " + ""

        };
    }

    public void addWarning(UUID uuid, String warning) {
        List<String> history = warnings.getOrDefault(uuid, new ArrayList<>());
        history.add("[" + Methods.getTime() + "] " + warning);
        warnings.put(uuid, history);
        save();
    }

    public void addJailing(UUID uuid, String jailing) {
        List<String> history = jails.getOrDefault(uuid, new ArrayList<>());
        history.add("[" + Methods.getTime() + "] " + jailing);
        jails.put(uuid, history);
        save();
    }

    public void addTempBan(UUID uuid, String tempban) {
        List<String> history = tempbans.getOrDefault(uuid, new ArrayList<>());
        history.add("[" + Methods.getTime() + "] " + tempban);
        tempbans.put(uuid, history);
        save();
    }

    public void addBan(UUID uuid, String ban) {
        List<String> history = bans.getOrDefault(uuid, new ArrayList<>());
        history.add("[" + Methods.getTime() + "] " + ban);
        bans.put(uuid, history);
        save();
    }

    public void save(){
        JsonMethod.save(new File(Main.dir, "/data"), "/history.dat", this);
    }

    public void load() {
        File file = new File(Main.dir, "/data");
        PlayerHistory data = JsonMethod.load(file, "/history.dat", PlayerHistory.class);
        if (data == null) {
            data = new PlayerHistory();
        }
        bans = data.bans;
        jails = data.jails;
        tempbans = data.tempbans;
        warnings = data.warnings;
    }

}
