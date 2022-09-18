package dev.elrol.serverutilities.init;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.ModInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PermRegistry {
    private transient final String fileName = "Permissions.json";
    public TreeMap<String, String> commandPerms = new TreeMap<>();
    public transient List<String> validPerms = new ArrayList<>();

    public Map<String, String> filterPerms(String rootNode){
        Map<String, String> m = commandPerms.entrySet().stream().filter(map -> map.getKey().split("_")[0].equals(rootNode)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Logger.debug(m.toString());
        return m;
    }

    public void save() {
        JsonMethod.save(ModInfo.Constants.configdir, fileName, this);
    }

    public void load(){
        PermRegistry reg = JsonMethod.load(ModInfo.Constants.configdir, fileName, PermRegistry.class);
        if (reg != null) {
            commandPerms = new TreeMap<>(reg.commandPerms);
            commandPerms.values().forEach(perm -> {
                if(!validPerms.contains(perm)) validPerms.add(perm);
            });
        }
        save();
    }

    public void add(String perm, boolean save) {
        add(perm);
        if(save) save();
    }

    public void add(String perm) {
        if(!validPerms.contains(perm)) validPerms.add(perm);
        Main.getLogger().debug("Permission Registered: \"" + perm + "\"");
    }

    public void add(String node, String perm) {
        if (!perm.isEmpty() && !node.isEmpty()) {
            if (!commandPerms.containsKey(node)) {
                commandPerms.put(node, perm);
                add(perm);
            }
        }
    }

    public void remove(String perm) {
        validPerms.remove(perm);
    }

    public void remove(String perm, boolean save) {
        remove(perm);
        if(save) save();
    }

    public String getPerm(String node) {
        return commandPerms.get(node);
    }

    public List<String> getPerms() {
        return validPerms;
    }
}

