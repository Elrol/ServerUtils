package dev.elrol.serverutilities.econ.chestshop;

import com.google.gson.JsonObject;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.api.econ.AbstractShop;
import dev.elrol.serverutilities.api.econ.IShopManager;
import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Methods;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class ChestShopManager implements IShopManager {
    public final ChestShopType type;
    public final Map<ResourceLocation, Map<BlockPos, ChestShop>> shopMap = new TreeMap<>();

    public ChestShopManager(ChestShopType type) {
        this.type = type;
        Main.permRegistry.add(type.perm, true);
    }

    @Override
    public String getFileName() {
        return getTag() + ".json";
    }

    @Override
    public String getTag() {
        return type.name();
    }

    @Override
    public void registerShop(Location loc, AbstractShop shop) {
        if(!(shop instanceof ChestShop chestShop)) return;
        if(isShop(loc)) return;

        BlockPos pos = loc.getBlockPos();
        Map<BlockPos, ChestShop> shops = shopMap.getOrDefault(loc.world, new HashMap<>());

        if(shops.containsKey(pos)) return;

        shops.put(pos, chestShop);
        shopMap.put(loc.world, shops);
    }

    @Override
    public Map<Location, AbstractShop> getPlayerShops(UUID uuid) {
        Map<Location, AbstractShop> chestShopMap = new HashMap<>();
        shopMap.forEach((dim, shops) -> {
            shops.forEach((pos, shop) -> {
                if(shop.getOwner().equals(uuid)) chestShopMap.put(new Location(dim, pos), shop);
            });
        });
        return chestShopMap;
    }

    @Override
    public boolean isShop(Location location) {
        Map<BlockPos, ChestShop> shops = shopMap.getOrDefault(location.world, new TreeMap<>());
        return shops.containsKey(location.getBlockPos());
    }

    @Override
    public AbstractShop getShop(Location loc) {
        Map<BlockPos, ChestShop> shops = shopMap.getOrDefault(loc.world, new HashMap<>());
        if(shops.containsKey(loc.getBlockPos())) {
            return shops.get(loc.getBlockPos());
        } else {
            ChestShop shop = new ChestShop(type);
            registerShop(loc, shop);
            return shop;
        }
    }

    @Override
    public void removeShop(Location loc) {
        Map<BlockPos, ChestShop> shops = shopMap.get(loc.world);
        if(shops == null) return;
        shops.remove(loc.getBlockPos());
    }

    @Override
    public AbstractShop parseShop(SignBlockEntity sign, Component[] messages) {
        try{
            float cost = Main.textUtils.parseCurrency(messages[3].getString());
            if(cost < 0) cost = 0f;

            ChestShop shop  = (ChestShop) getShop(new Location(sign));
            shop.setCost(cost);
            shop.updateSign(sign);
            return shop;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
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
        Map<ResourceLocation, Map<BlockPos, ChestShop>> newShopMap = new TreeMap<>();
        JsonObject mapObj = JsonMethod.newLoad(dir, getFileName(), JsonObject.class);

        if(mapObj == null) return;

        mapObj.entrySet().forEach(entry -> {
            Map<BlockPos, ChestShop> shops = new TreeMap<>();
            entry.getValue().getAsJsonObject().entrySet().forEach(shopEntry -> shops.put(Methods.stringToPos(shopEntry.getKey()), ChestShop.fromJson(type, shopEntry.getValue().getAsJsonObject())));
            newShopMap.put(new ResourceLocation(entry.getKey()), shops);
        });
        shopMap.clear();
        shopMap.putAll(newShopMap);
    }
}
