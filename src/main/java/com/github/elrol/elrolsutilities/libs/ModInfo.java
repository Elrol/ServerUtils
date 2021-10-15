package com.github.elrol.elrolsutilities.libs;

import java.io.File;
import java.util.UUID;

import com.github.elrol.elrolsutilities.config.Configs;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.loading.FMLPaths;

public class ModInfo {
    public static final String MODID = "serverutilities";
    public static final String VERSION = "v1.4.5";
    public static final String DESC = "A small utility mod";
    private static final String JNEM = TextFormatting.DARK_GRAY + "[" + TextFormatting.LIGHT_PURPLE + "JNEM" + TextFormatting.DARK_GRAY + "]" + TextFormatting.RESET + ": ";
    public static class Constants {
        public static final int RTP_Short_Range = 1000;
        public static final int RTP_Med_Range = 5000;
        public static final int RTP_Long_Range = 10000;
        public static final int RTP_Max_Attempts = 5;
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

