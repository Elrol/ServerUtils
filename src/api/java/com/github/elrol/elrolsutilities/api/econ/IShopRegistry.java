package dev.elrol.serverutilities.api.econ;

import dev.elrol.serverutilities.api.data.Location;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public interface IShopRegistry {
    void registerShopManager(dev.elrol.serverutilities.api.econ.IShopManager shop);
    dev.elrol.serverutilities.api.econ.IShopManager getShopManager(String tag);
    dev.elrol.serverutilities.api.econ.IShopManager getShopManager(Location loc);
    boolean isShop(SignBlockEntity sign);
    boolean exists(Location loc);
    dev.elrol.serverutilities.api.econ.AbstractShop parseSign(SignBlockEntity sign);
    dev.elrol.serverutilities.api.econ.AbstractShop getShop(Location loc);

    void removeShop(Location loc);

    void save();
}
