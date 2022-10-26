package dev.elrol.serverutilities.api.econ;

import dev.elrol.serverutilities.api.data.Location;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public interface IShopRegistry {
    void registerShopManager(IShopManager shop);
    IShopManager getShopManager(String tag);
    IShopManager getShopManager(Location loc);
    boolean isShop(SignBlockEntity sign);
    boolean exists(Location loc);
    AbstractShop parseSign(SignBlockEntity sign);
    AbstractShop getShop(Location loc);

    void removeShop(Location loc);

    void save();
}
