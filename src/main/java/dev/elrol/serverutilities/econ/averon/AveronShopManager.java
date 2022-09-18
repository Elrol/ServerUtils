package dev.elrol.serverutilities.econ.averon;

import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.api.econ.AbstractShop;
import dev.elrol.serverutilities.api.econ.IShopManager;
import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class AveronShopManager implements IShopManager {

    public Map<ResourceLocation, Map<BlockPos, AveronShop>> shopMap = new HashMap<>();

    @Override
    public String getFileName() {
        return getTag() + ".json";
    }

    @Override
    public String getTag() {
        return "Averon";
    }

    @Override
    public void registerShop(Location loc, AbstractShop shop) {
        if(!(shop instanceof AveronShop)) return;
        ResourceLocation world = loc.world;
        BlockPos pos = loc.getBlockPos();
        Map<BlockPos, AveronShop> map = shopMap.getOrDefault(world, new HashMap<>());
        if(map.containsKey(pos)) return;
        map.put(pos, (AveronShop) shop);
        shopMap.put(world, map);
    }

    @Override
    public Map<Location, AbstractShop> getPlayerShops(UUID uuid) {
        Map<Location, AbstractShop> newMap = new HashMap<>();
        shopMap.forEach((dim,map) -> map.forEach((pos, shop) -> {
            if(shop.getOwner().equals(uuid)) newMap.put(new Location(dim, pos, 0, 0), shop);
        }));
        return newMap;
    }

    @Override
    public boolean isShop(Location location) {
        return shopMap.getOrDefault(location.world, new HashMap<>()).containsKey(location.getBlockPos());
    }

    @Override
    public AbstractShop getShop(Location loc) {
        Map<BlockPos, AveronShop> shops = shopMap.getOrDefault(loc.world, new HashMap<>());
        if(shops.containsKey(loc.getBlockPos())) {
            return shops.get(loc.getBlockPos());
        } else {
            AveronShop shop = new AveronShop();
            registerShop(loc, shop);
            return shop;
        }
    }

    @Override
    public void removeShop(Location loc) {
        shopMap.getOrDefault(loc.world, new HashMap<>()).remove(loc.getBlockPos());
    }

    @Override
    public AbstractShop parseShop(SignBlockEntity sign, Component[] messages) {
        if(messages[3] != null && !messages[3].getString().isEmpty()) {
            String costText = messages[3].getString();
            if(costText.startsWith("$")) costText = costText.substring(1);
            float cost = Float.parseFloat(costText);

            if(cost < 0) Logger.log("Cost for Averon Sign Shop is below zero");
            else Logger.log("Cost for Averon Sign Shop is: " + cost);

            AbstractShop shop = getShop(new Location(sign));
            shop.setCost(cost);

            shop.updateSign(sign);

            return shop;
        } else {
            Logger.err("Cost for Averon Sign Shop is missing");
        }
        return null;
    }

    @Override
    public void save() {
        JsonObject mapObj = new JsonObject();

        shopMap.forEach((dim, shops) -> {
            JsonObject shopsObj = new JsonObject();
            shops.forEach((pos, shop) -> shopsObj.add(Methods.posToString(pos), shop.toJson()));
            mapObj.add(dim.toString(), shopsObj);
        });

        JsonMethod.newSave(dir, getFileName(), mapObj);
    }

    @Override
    public void load() {
        Map<ResourceLocation, Map<BlockPos, AveronShop>> newShopMap = new HashMap<>();
        JsonObject obj = JsonMethod.newLoad(dir, getFileName(), JsonObject.class);

        if(obj == null) return;

        obj.entrySet().forEach(entry -> {
            ResourceLocation rs = ResourceLocation.tryParse(entry.getKey());
            Map<BlockPos, AveronShop> shops = new TreeMap<>();
            JsonObject shopsObj = entry.getValue().getAsJsonObject();
            shopsObj.entrySet().forEach(shopsEntry -> {
                BlockPos pos = Methods.stringToPos(shopsEntry.getKey());
                AveronShop shop = AveronShop.fromJson(shopsEntry.getValue().getAsJsonObject());
                shops.put(pos, shop);
            });
            newShopMap.put(rs, shops);
        });

        shopMap = newShopMap;
    }
}
