package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlackLists {
    private transient static File bldir = new File(FMLPaths.CONFIGDIR.get().toFile(), "/serverutils/blacklists");
    private transient static String repairDir = "/repair-blacklist.json";

    public List<String> blacklist;

    public void save() {
        if (!bldir.exists()) {
            bldir.mkdirs();
        }
        JsonMethod.save(bldir, repairDir, this);
    }

    public void load() {
        if (!bldir.exists()) {
            bldir.mkdirs();
        }
        try {
            BlackLists bl = JsonMethod.load(bldir, repairDir, BlackLists.class);
            if(bl != null) {
                blacklist = bl.blacklist;
                if(blacklist == null) {
                    blacklist = new ArrayList<>();
                    save();
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

}

