package com.github.elrol.elrolsutilities.api.econ;

import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.Location;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public interface IShopManager {

    File dir = new File(IElrolAPI.getInstance().getDataDir(), "/data/shops");
    String getFileName();
    String getTag();

    void registerShop(Location loc, AbstractShop shop);

    Map<Location, AbstractShop> getPlayerShops(UUID uuid);

    boolean isShop(Location location);

    AbstractShop getShop(Location loc);

    void removeShop(Location loc);

    AbstractShop parseShop(SignBlockEntity sign, Component[] messages);

    void save();

    void load();

}
