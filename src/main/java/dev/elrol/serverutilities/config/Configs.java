package dev.elrol.serverutilities.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.libs.ModInfo;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;
import java.io.IOException;

public class Configs {
    private static final ForgeConfigSpec.Builder featureBuilder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec featureConfig;

    private static final ForgeConfigSpec.Builder commandBuilder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec commandConfig;


    static {
        Main.getLogger().info("Loading configs");
        FeatureConfig.init(featureBuilder);
        featureConfig = featureBuilder.build();

        CommandConfig.init(commandBuilder);
        commandConfig = commandBuilder.build();
    }

    public static void reload(){
        loadConfig(featureConfig, new File(ModInfo.Constants.configdir, "/Features.toml"));
        loadConfig(commandConfig, new File(ModInfo.Constants.configdir, "/Commands.toml"));
    }

    public static void loadConfig(ForgeConfigSpec config, File file){
        Main.getLogger().info("Loading config: " + file.toString());
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final CommentedFileConfig configFile = CommentedFileConfig.builder(file).sync().autosave().writingMode(WritingMode.REPLACE).preserveInsertionOrder().build();
        Main.getLogger().info("Built config: " + file);
        file.getParentFile().mkdirs();
        configFile.load();
        Main.getLogger().info("Loaded config: " + file);
        config.setConfig(configFile);
    }
}
