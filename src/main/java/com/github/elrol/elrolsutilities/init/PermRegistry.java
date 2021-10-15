package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import com.google.gson.reflect.TypeToken;

import java.util.*;
import java.util.stream.Collectors;

public class PermRegistry {
    private final String fileName = "Permissions.json";
    public Map<String, String> commandPerms = new HashMap<>();
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
            commandPerms = reg.commandPerms;
            commandPerms.values().forEach(perm -> {
                if(!validPerms.contains(perm)) validPerms.add(perm);
            });
        }
    }

    public void add(String perm) {
        validPerms.add(perm);
        Main.getLogger().info("Permission Registered: \"" + perm + "\"");
    }

    public void add(String node, String perm) {
        if (!perm.isEmpty() && !node.isEmpty()) {
            if (!commandPerms.containsKey(node)) {
                commandPerms.put(node, perm);
                add(perm);
                save();
            }
        }
    }

    public String getPerm(String node) {
        return commandPerms.get(node);
    }

    public List<String> getPerms() {
        return validPerms;
    }
}

