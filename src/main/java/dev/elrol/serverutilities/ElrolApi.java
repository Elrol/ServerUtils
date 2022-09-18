package dev.elrol.serverutilities;

import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.claims.IClaimManager;
import dev.elrol.serverutilities.api.claims.IClaimSettingRegistry;
import dev.elrol.serverutilities.api.data.IPlayerDatabase;
import dev.elrol.serverutilities.api.econ.IShopRegistry;
import dev.elrol.serverutilities.api.init.ICommandRegistry;
import dev.elrol.serverutilities.api.init.ITextUtils;
import dev.elrol.serverutilities.api.perms.IPermissionHandler;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.init.ClaimManager;
import dev.elrol.serverutilities.init.ClaimSettingRegistry;
import dev.elrol.serverutilities.libs.ModInfo;
import dev.elrol.serverutilities.libs.PermissionHandler;

import java.io.File;

public class ElrolApi implements IElrolAPI {

    @Override
    public IPermissionHandler getPermissionHandler() {
        return new PermissionHandler();
    }

    @Override
    public IShopRegistry getShopInit() { return Main.shopRegistry; }

    @Override
    public IClaimManager getClaimManager() { return new ClaimManager(); }

    @Override
    public IClaimSettingRegistry getClaimSettingRegistry() { return new ClaimSettingRegistry(); }

    @Override
    public IPlayerDatabase getPlayerDatabase() {
        return Main.database;
    }

    public ICommandRegistry getCommandRegistry() {
        return Main.commandRegistry;
    }

    public boolean enableTranslations() {
        return FeatureConfig.translation_enable.get();
    }

    public ITextUtils getTextUtils() {
        return Main.textUtils;
    }

    @Override
    public File getDataDir() {
        return Main.dir;
    }

    public File getConfigDir() { return ModInfo.Constants.configdir; }
}
