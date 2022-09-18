package dev.elrol.serverutilities.init;

import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.api.econ.AbstractShop;
import dev.elrol.serverutilities.api.econ.IShopManager;
import dev.elrol.serverutilities.api.econ.IShopRegistry;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.SignUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShopRegistry implements IShopRegistry {

    Map<String, IShopManager> shopMap = new HashMap<>();
    public static Map<UUID, Location> confirmMap = new HashMap<>();

    @Override
    public void registerShopManager(IShopManager shop) {
        Logger.log("Registering " + shop.getTag());
        shop.load();
        shopMap.put(shop.getTag().toLowerCase(), shop);
        System.out.print(shopMap);
    }

    @Override
    public IShopManager getShopManager(String tag) {
        System.out.print(shopMap);
        return shopMap.get(tag.toLowerCase());
    }

    @Override
    public boolean isShop(SignBlockEntity sign) {
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
        if(!tag.isEmpty() && shopMap.containsKey(tag.toLowerCase())) return tag;
        return "";
    }

    public boolean exists(Location loc) {
        for(Map.Entry<String, IShopManager> entry : shopMap.entrySet()) {
            if(entry.getValue().isShop(loc)) return true;
        }
        return false;
    }

    @Override
    public AbstractShop parseSign(SignBlockEntity sign) {
        Component[] messages = new Component[SignBlockEntity.LINES];
        for(int i = 0; i < messages.length; i++) {
            messages[i] = sign.getMessage(i, false);
        }

        Component textComp = messages[0];
        String tag = ChatFormatting.stripFormatting(textComp.getString());
        if(tag == null) {
            Logger.err("Tag was stripped of formatting and resulted in a null");
            return null;
        }
        if(tag.contains("[") && tag.contains("]")) {
            tag = tag.substring(1, tag.length() - 1);

            IShopManager manager = getShopManager(tag);
            if(manager == null) return null;
            return manager.parseShop(sign, messages);
        }
        return null;
    }

    public AbstractShop getShop(Location loc) {
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

    public void save() {
        Logger.log("Saving Shop Registries");
        shopMap.forEach((tag, handler) -> handler.save());
    }
}
