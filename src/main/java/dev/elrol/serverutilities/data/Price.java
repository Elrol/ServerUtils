package dev.elrol.serverutilities.data;

public class Price {

    public float buy;
    public float sell;

    public Price(float buyPrice, float sellPrice){
        buy = buyPrice;
        sell = sellPrice;
    }

    public String toString() {
        return "Buy Price: " + buy + ", Sell Price: " + sell;
    }
}
