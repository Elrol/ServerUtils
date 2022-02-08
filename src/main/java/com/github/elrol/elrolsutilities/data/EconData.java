package com.github.elrol.elrolsutilities.data;

import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class EconData {

    public Map<String, Price> prices = new HashMap<>();

    public Price getPrice(ResourceLocation location) {
        return prices.getOrDefault(location.toString(), new Price(0f,0f));
    }

    public Price setPrice(ResourceLocation location, float buy, float sell) {
        Price price = new Price(buy, sell);
        prices.put(location.toString(), price);
        save();
        return price;
    }

    public void setBuyPrice(ResourceLocation location, float cost) {
        Price price = getPrice(location);
        price.buy = cost;
        prices.put(location.toString(), price);
        save();
    }

    public void setSellPrice(ResourceLocation location, float cost) {
        Price price = getPrice(location);
        price.sell = cost;
        prices.put(location.toString(), price);
        save();
    }

    public float getSellPrice(ResourceLocation location) {
        Price price = getPrice(location);
        return price.sell;
    }

    public float getBuyPrice(ResourceLocation location) {
        Price price = getPrice(location);
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
