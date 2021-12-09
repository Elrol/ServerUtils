package com.github.elrol.elrolsutilities;

import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.claims.IClaimManager;
import com.github.elrol.elrolsutilities.api.claims.IClaimSettingRegistry;
import com.github.elrol.elrolsutilities.api.data.IPlayerDatabase;
import com.github.elrol.elrolsutilities.api.econ.IShopRegistry;
import com.github.elrol.elrolsutilities.api.perms.IPermissionHandler;
import com.github.elrol.elrolsutilities.init.ClaimManager;
import com.github.elrol.elrolsutilities.init.ClaimSettingRegistry;
import com.github.elrol.elrolsutilities.libs.PermissionHandler;

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

    @Override
    public File getDataDir() {
        return Main.dir;
    }

}
