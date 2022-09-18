package dev.elrol.serverutilities.data;

import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.ModInfo;

import java.util.HashMap;
import java.util.Map;

public class EconData {

    public Map<String, Price> prices = new HashMap<>();

    public Price getPrice(String id) {
        return prices.getOrDefault(id, new Price(0f,0f));
    }

    public Price setPrice(String id, float buy, float sell) {
        Price price = new Price(buy, sell);
        prices.put(id, price);
        save();
        return price;
    }

    public void setBuyPrice(String id, float cost) {
        Price price = getPrice(id);
        price.buy = cost;
        prices.put(id, price);
        save();
    }

    public void setSellPrice(String id, float cost) {
        Price price = getPrice(id);
        price.sell = cost;
        prices.put(id, price);
        save();
    }

    public float getSellPrice(String location) {
        Price price = getPrice(location);
        return price.sell;
    }

    public float getBuyPrice(String id) {
        Price price = getPrice(id);
        return price.buy;
    }

    public void save(){
        JsonMethod.save(ModInfo.Constants.configdir, "/Prices.json", prices);
    }

    public void load() {
        EconData data = JsonMethod.load(ModInfo.Constants.configdir, "/DefaultPrices.json", EconData.class);
        if(data != null) prices = data.prices;
        save();
    }
}
