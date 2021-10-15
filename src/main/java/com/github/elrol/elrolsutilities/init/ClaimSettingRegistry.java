package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.claims.IClaimSettingEntry;
import com.github.elrol.elrolsutilities.api.claims.IClaimSettingRegistry;

import java.util.HashMap;
import java.util.Map;

public class ClaimSettingRegistry implements IClaimSettingRegistry {

    public Map<String, IClaimSettingEntry> entries = new HashMap<>();

    public ClaimSettingRegistry() {

    }

    public void register(String name, IClaimSettingEntry setting) {
        if(!entries.containsKey(name)) {
            entries.put(name, setting);
            return;
        }
        Main.getLogger().error("The " + name + " claim setting has already been registered.");
    }

    public IClaimSettingEntry get(String name) {
        return entries.get(name);
    }

}
