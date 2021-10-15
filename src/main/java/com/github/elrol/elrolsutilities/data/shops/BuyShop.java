package com.github.elrol.elrolsutilities.data.shops;

import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.data.ChestShop;
import com.github.elrol.elrolsutilities.libs.SignUtils;
import net.minecraft.tileentity.SignTileEntity;

import java.util.UUID;

public class BuyShop extends ChestShop {


    public BuyShop(UUID uuid, Location loc) {
        super(uuid, loc);
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public float getCost() {
        return 0;
    }

    @Override
    public String getTag() {
        return "buy";
    }

    @Override
    public Location getLoc() {
        return location;
    }

    @Override
    public Location getLinkLoc() {
        return null;
    }

    @Override
    public boolean hitShop() {
        return false;
    }

    @Override
    public boolean useShop() {
        return false;
    }

    @Override
    public void parseSign(SignTileEntity sign) {
        String tag = SignUtils.readLine(sign, 1);
        if(!tag.equalsIgnoreCase("[" + getTag() + "]")) return;
        super.parseSign(sign);
    }

}
