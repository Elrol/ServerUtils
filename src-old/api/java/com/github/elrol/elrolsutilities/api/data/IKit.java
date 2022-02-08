package com.github.elrol.elrolsutilities.api.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;

public interface IKit {

    String name = "";
    List<CompoundNBT> kit = new ArrayList<>();
    int cooldown = 0;
    boolean isDefault = false;
    int cost = 0;

    List<ItemStack> getKit();
    boolean addItem(ItemStack stack);
    boolean removeItem(ItemStack stack);
    void setKit(List<ItemStack> newKit);
    void clearKit();
    void setCost(int newCost);
    int getCost();
    void save();
    void give(ServerPlayerEntity player);
    void setDefault(boolean flag);
    boolean isDefault();
}
