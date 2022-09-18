package dev.elrol.serverutilities.data;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.JsonMethod;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DefaultTitles {

    private static final File titleDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "/serverutils/");
    private final Map<String,String> defaultTitles = new HashMap<>();

    public Map<String,String> get() { return defaultTitles; }

    public void load() {
        DefaultTitles titles = JsonMethod.load(titleDir, "Titles.json", DefaultTitles.class);
        if(titles == null) {
            defaultTitles.put("example", "&2Example &aTitle");
            titles = this;
            save();
        }
        defaultTitles.putAll(titles.defaultTitles);
        defaultTitles.forEach((name,title)->
            Main.permRegistry.add("title." + name, true)
        );
    }

    public void save() {
        JsonMethod.save(titleDir, "Titles.json", this);
    }

}
