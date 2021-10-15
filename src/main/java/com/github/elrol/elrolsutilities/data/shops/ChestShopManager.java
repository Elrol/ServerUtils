package com.github.elrol.elrolsutilities.data.shops;

import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.api.econ.IShop;
import com.github.elrol.elrolsutilities.api.econ.IShopManager;

import java.util.Map;
import java.util.UUID;

public class ChestShopManager {

    public static class Buy implements IShopManager {

        @Override
        public String getTag() {
            return "buy";
        }

        @Override
        public IShop newShop(UUID owner, Location loc) {
            Map<Location, IShop> shops = getPlayerShops(owner);
            if(shops.containsKey(loc)) {
                return null;
            } else {
                BuyShop shop = new BuyShop(owner, loc);
                registerShop(shop);
                return shop;
            }
        }

    }

    public static class Sell implements IShopManager {

        @Override
        public String getTag() {
            return null;
        }

        @Override
        public IShop newShop(UUID owner, Location loc) {
            return null;
        }

    }

    public static class AdminBuy implements IShopManager {

        @Override
        public String getTag() {
            return null;
        }

        @Override
        public IShop newShop(UUID owner, Location loc) {
            return null;
        }

    }

    public static class AdminSell implements IShopManager {

        @Override
        public String getTag() {
            return null;
        }

        @Override
        public IShop newShop(UUID owner, Location loc) {
            return null;
        }

    }
}
