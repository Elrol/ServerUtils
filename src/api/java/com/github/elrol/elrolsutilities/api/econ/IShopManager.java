package dev.elrol.serverutilities.api.econ;

import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.Location;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public interface IShopManager {

    File dir = new File(IElrolAPI.getInstance().getDataDir(), "/data/shops");
    String getFileName();
    String getTag();

    void registerShop(Location loc, dev.elrol.serverutilities.api.econ.AbstractShop shop);

    Map<Location, dev.elrol.serverutilities.api.econ.AbstractShop> getPlayerShops(UUID uuid);

    boolean isShop(Location location);

    dev.elrol.serverutilities.api.econ.AbstractShop getShop(Location loc);

    void removeShop(Location loc);

    dev.elrol.serverutilities.api.econ.AbstractShop parseShop(SignBlockEntity sign, Component[] messages);

    void save();

    void load();

}
