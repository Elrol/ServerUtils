package dev.elrol.serverutilities.econ.chestshop;

public enum ChestShopType {
    Buy("shop.chest.user.buy"),
    Sell("shop.chest.user.sell"),
    AdminBuy("shop.chest.admin.buy"),
    AdminSell("shop.chest.admin.sell");

    String perm;
    ChestShopType(String perm) { this.perm = perm; }
}
