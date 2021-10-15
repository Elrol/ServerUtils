package com.github.elrol.elrolsutilities.api;

import com.github.elrol.elrolsutilities.api.claims.IClaimManager;
import com.github.elrol.elrolsutilities.api.claims.IClaimSettingRegistry;
import com.github.elrol.elrolsutilities.api.econ.IShopRegistry;
import com.github.elrol.elrolsutilities.api.perms.IPermissionHandler;

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
}
