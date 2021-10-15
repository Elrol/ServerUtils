package com.github.elrol.elrolsutilities.api.econ;

import com.github.elrol.elrolsutilities.api.data.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface IShopManager {

    Map<Location, IShop> shopMap = new HashMap<>();

    String getTag();

    default void registerShop(IShop shop) {
        if(shopMap.containsKey(shop.getLoc())) return;
        shopMap.put(shop.getLoc(), shop);
    }

    IShop newShop(UUID owner, Location loc);

    default Map<Location, IShop> getPlayerShops(UUID uuid) {
        Map<Location, IShop> newMap = new HashMap<>();
        shopMap.values().forEach(shop -> {
            if(shop.getOwner().equals(uuid)) newMap.put(shop.getLoc(), shop);
        });
        return newMap;
    }

    default IShop getPlayerShop(UUID uuid, Location location) {
        return getPlayerShops(uuid).getOrDefault(location, null);
    }

    default boolean isShop(Location location) {
        return shopMap.containsKey(location);
    }

    default IShop getShop(Location loc) {
        return shopMap.get(loc);
    }

    default void removeShop(Location loc) {
        shopMap.remove(loc);
    }
}
