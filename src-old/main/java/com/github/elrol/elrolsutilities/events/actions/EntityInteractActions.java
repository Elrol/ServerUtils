package com.github.elrol.elrolsutilities.events.actions;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.text.ITextComponent;

public class EntityInteractActions {
    public static boolean nameMinecart(AbstractMinecartEntity cart, ITextComponent name) {
        if (name != null) {
            cart.setCustomName(name);
            cart.setCustomNameVisible(true);
            return true;
        }
        return false;
    }
}

