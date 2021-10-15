package com.github.elrol.elrolsutilities.api.econ;

import com.github.elrol.elrolsutilities.api.data.Location;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.SignTileEntity;

import java.util.UUID;

public interface IShop {

    /**
     * @return Owner's Unique ID
     */
    UUID getOwner();

    /**
     * @return The cost to use the shop
     */
    float getCost();

    /**
     * @return The tag required to create the shop
     */
    String getTag();

    /**
     * @return Location of the sign
     */
    Location getLoc();

    /**
     * @return Location of the linked block
     */
    Location getLinkLoc();

    /**
     * Called when the sign is hit. Returns true if allowed to break and false if not.
     * @return boolean
     */
    boolean hitShop();

    /**
     * Called when the sign is used. Returns true if allowed to use and false if not.
     * @return boolean
     */
    boolean useShop();

    /**
     * Uses a SignTileEntity to set values for the shop
     * @param sign Sign to be parsed
     */
    void parseSign(SignTileEntity sign);

    boolean canEdit(ServerPlayerEntity player);

    boolean isAdmin();
}
