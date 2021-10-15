package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.api.econ.IShop;
import com.github.elrol.elrolsutilities.api.econ.IShopManager;
import com.github.elrol.elrolsutilities.api.econ.IShopRegistry;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.SignUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.SignTileEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShopRegistry implements IShopRegistry {

    private static transient final File dir = new File(Main.dir, "/data/");

    Map<String, IShopManager> shopMap = new HashMap<>();

    @Override
    public void registerShopManager(IShopManager shop) {
        shopMap.put(shop.getTag(), shop);
    }

    @Override
    public IShopManager getShopManager(String tag) {
        return shopMap.get(tag);
    }

    @Override
    public boolean isShop(SignTileEntity sign) {
        String tag = getTagFromString(SignUtils.readLine(sign, 1));

        IShopManager shopManager = getShopManager(tag);
        if(shopManager != null && sign.getLevel() != null) {
            return shopManager.isShop(new Location(sign.getLevel().dimension(), sign.getBlockPos(), 0 ,0));
        }
        return false;
    }

    public String getTagFromString(String string) {
        String tag = "";
        if(string.startsWith("[") && string.endsWith("]")) {
            tag = string.substring(1, string.length() - 1);
        }
        if(!tag.isEmpty() && shopMap.containsKey(tag)) return tag;
        return "";
    }

    public boolean exists(Location loc) {
        return false;
    }

    public IShop getShop(Location loc) {
        IShopManager manager = getShopManager(loc);
        if(manager == null) return null;
        return manager.getShop(loc);
    }

    @Override
    public void removeShop(Location loc) {
        IShopManager manager = getShopManager(loc);
        manager.removeShop(loc);
    }

    public IShopManager getShopManager(Location loc) {
        for(IShopManager manager : shopMap.values()) {
            if(manager.isShop(loc)) return manager;
        }
        return null;
    }

    public void load(){
        ShopRegistry shopRegistry = JsonMethod.load(dir, "shopRegistry.dat", ShopRegistry.class);
        if(shopRegistry != null) shopMap = shopRegistry.shopMap;

    }

    public void save(){
        JsonMethod.save(dir, "shopRegistry.dat", this);
    }
}
