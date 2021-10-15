package com.github.elrol.elrolsutilities.init;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.google.gson.reflect.TypeToken;

import net.minecraftforge.fml.loading.FMLPaths;

public class BlackLists {
    private transient static File bldir = new File(FMLPaths.CONFIGDIR.get().toFile(), "/serverutils/blacklists");
    private transient static String repairDir = "/repair-blacklist.json";

    public List<String> repair;

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
        BlackLists bl = JsonMethod.load(bldir, repairDir, BlackLists.class);
        if (repair == null) {
            repair = new ArrayList<>();
            save();
        }
    }

}

