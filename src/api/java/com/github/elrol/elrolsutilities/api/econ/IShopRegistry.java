package com.github.elrol.elrolsutilities.api.econ;

import com.github.elrol.elrolsutilities.api.data.Location;
import net.minecraft.tileentity.SignTileEntity;

public interface IShopRegistry {
    void registerShopManager(IShopManager shop);
    IShopManager getShopManager(String tag);
    IShopManager getShopManager(Location loc);
    boolean isShop(SignTileEntity sign);
    boolean exists(Location loc);
    AbstractShop parseSign(SignTileEntity sign);
    AbstractShop getShop(Location loc);

    void removeShop(Location loc);

    void save();
}
