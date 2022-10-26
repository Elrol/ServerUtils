package dev.elrol.serverutilities.init;

import com.google.gson.JsonSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Logger;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.*;

public class Blacklists {
    private final transient static File bldir = new File(FMLPaths.CONFIGDIR.get().toFile(), "/serverutils/blacklists");
    private final transient static String dir = "/blacklists.json";

    public List<String> repair_blacklist = new ArrayList<>();
    public List<String> clearlag_item_blacklist = new ArrayList<>();
    public List<String> clearlag_entity_blacklist = new ArrayList<>();
    public Map<String, List<String>> dimension_command_blacklist = new HashMap<>();

    public Blacklists() {
        clearlag_item_blacklist.add("minecraft:iron_pickaxe");

        List<String> cmds = Collections.singletonList("test command");
        Main.mcServer.levelKeys().forEach(key->{
            dimension_command_blacklist.put(key.location().toString(), cmds);
        });
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
                clearlag_entity_blacklist = bl.clearlag_entity_blacklist;
                clearlag_item_blacklist = bl.clearlag_item_blacklist;
                repair_blacklist = bl.repair_blacklist;
                dimension_command_blacklist = bl.dimension_command_blacklist;
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

