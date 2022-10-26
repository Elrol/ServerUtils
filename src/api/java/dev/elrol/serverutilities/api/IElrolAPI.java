package dev.elrol.serverutilities.api;

import dev.elrol.serverutilities.api.claims.IClaimManager;
import dev.elrol.serverutilities.api.claims.IClaimSettingRegistry;
import dev.elrol.serverutilities.api.data.IPlayerDatabase;
import dev.elrol.serverutilities.api.econ.IShopRegistry;
import dev.elrol.serverutilities.api.init.ICommandRegistry;
import dev.elrol.serverutilities.api.init.ITextUtils;
import dev.elrol.serverutilities.api.perms.IPermissionHandler;

import java.io.File;

public interface IElrolAPI {

    static IElrolAPI getInstance() {
        return ElrolAPIProxy.getProxy().getApi();
    }

    static void setInstance(IElrolAPI api) {
        ElrolAPIProxy.getProxy().setApi(api);
    }

    IPermissionHandler getPermissionHandler();

    IShopRegistry getShopInit();

    IClaimManager getClaimManager();

    IClaimSettingRegistry getClaimSettingRegistry();

    IPlayerDatabase getPlayerDatabase();

    ICommandRegistry getCommandRegistry();

    boolean enableTranslations();

    ITextUtils getTextUtils();

    File getDataDir();

    File getConfigDir();
}
