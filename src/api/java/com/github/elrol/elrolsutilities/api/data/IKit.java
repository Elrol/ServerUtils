package dev.elrol.serverutilities.api.data;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface IKit {

    String name = "";
    List<CompoundTag> kit = new ArrayList<>();
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
    void give(ServerPlayer player);
    void setDefault(boolean flag);
    boolean isDefault();
}
