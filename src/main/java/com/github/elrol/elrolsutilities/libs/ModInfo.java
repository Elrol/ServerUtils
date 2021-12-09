package com.github.elrol.elrolsutilities.libs;

import com.github.elrol.elrolsutilities.config.Configs;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.UUID;

public class ModInfo {
    public static class Constants {
        public static final UUID ownerUUID = UUID.fromString("06fe1033-d391-4d21-8706-410e2c22e8a8");
        public static final File kitdir = new File(FMLPaths.CONFIGDIR.get().toFile(), "/serverutils/kits");
        public static final File configdir = new File(FMLPaths.CONFIGDIR.get().toFile(), "/serverutils");
        public static final File rankdir = new File(configdir, "/ranks/");
        public static final String ftbrankId = "ftbranks";
        public static final String ftbchunkId = "ftbchunks";
    }

    public static String getRawTag(){
        try{
            return FeatureConfig.tag.get();
        } catch (Exception e){
            if(Configs.featureConfig == null) return "&8[&9S&aU&8]";
           return Configs.featureConfig.get("tag").toString();
        }
    }

    public static String getTag(){
        return TextUtils.formatString(getRawTag() + "&r ");
    }

}

