package dev.elrol.serverutilities.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.ModInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DimensionGamemodes {

    private static transient final File file = new File(ModInfo.Constants.configdir, "DimensionGameModes.json");
    private static transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Map<String,String> data = new HashMap<>();

    @Nullable
    public GameType getMode(ResourceLocation dim) {
        String mode = data.getOrDefault(dim.toString(), "");
        if(mode.isEmpty()) return null;
        return GameType.byName(mode, GameType.SURVIVAL);
    }

    public static DimensionGamemodes load() {
        DimensionGamemodes dimMode = new DimensionGamemodes();

        if(file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                dimMode = gson.fromJson(reader, DimensionGamemodes.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(dimMode.data.isEmpty()) {
            DimensionGamemodes finalDimMode = dimMode;
            Main.mcServer.levelKeys().forEach(key-> finalDimMode.data.put(key.location().toString(), ""));
            dimMode.save();
        }

        return dimMode;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
