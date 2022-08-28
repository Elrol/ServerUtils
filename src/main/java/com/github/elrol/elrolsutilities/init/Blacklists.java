package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Blacklists {
    private final transient static File bldir = new File(FMLPaths.CONFIGDIR.get().toFile(), "/serverutils/blacklists");
    private final transient static String dir = "/blacklists.json";

    public List<String> repair_blacklist = new ArrayList<>();
    public List<String> clearlag_item_blacklist = new ArrayList<>();
    public List<String> clearlag_entity_blacklist = new ArrayList<>();

    public Blacklists() {
        clearlag_item_blacklist.add("minecraft:iron_pickaxe");
    }

    public void save() {
        if (bldir.mkdirs()) {
            Logger.debug("Blacklist folder created");
        }
        JsonMethod.save(bldir, dir, this);
    }

    public void reload() {
        try {
            Blacklists bl = JsonMethod.load(bldir, dir, Blacklists.class);
            if(bl != null) {
                this.clearlag_entity_blacklist = bl.clearlag_entity_blacklist;
                this.clearlag_item_blacklist = bl.clearlag_item_blacklist;
                this.repair_blacklist = bl.repair_blacklist;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Blacklists load() {
        if (bldir.mkdirs()) {
            Logger.debug("Blacklist folder created");
        }
        try {
            Blacklists bl = JsonMethod.load(bldir, dir, Blacklists.class);
            if(bl != null) {
                return bl;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        Blacklists bl = new Blacklists();
        bl.save();
        return bl;
    }

}

