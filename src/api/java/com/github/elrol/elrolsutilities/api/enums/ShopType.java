package com.github.elrol.elrolsutilities.api.enums;

public enum ShopType {
    Buy("Buy"),
    Sell("Sell"),
    AdminBuy("AdminBuy"),
    AdminSell("AdminSell");

    public String name;

    ShopType(String name){
        this.name = name;
    }

    public String get() {
        return name;
    }

    public ShopType get(String name) {
        for(ShopType type : values()) {
            if(type.name.equalsIgnoreCase(name)) return type;
        }
        return null;
    }

    @Override
    public String toString() {
        return "ShopType{" +
                "name='" + name + '\'' +
                '}';
    }
}
