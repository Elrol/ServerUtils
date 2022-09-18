package dev.elrol.serverutilities.events.actions;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class EntityInteractActions {
    public static boolean nameMinecart(AbstractMinecart cart, Component name) {
        if (name != null) {
            cart.setCustomName(name);
            cart.setCustomNameVisible(true);
            return true;
        }
        return false;
    }
}

