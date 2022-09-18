package dev.elrol.serverutilities.data;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IKit;
import dev.elrol.serverutilities.api.perms.IPermission;
import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.ModInfo;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Kit implements IKit {
    public String name;
    private List<CompoundTag> kit;
    public int cooldown;
    private boolean isDefault;
    private int cost = 0;

    public Kit(String name) {
        this.name = name;
        this.kit = new ArrayList<>();
        this.save();
    }

    public List<ItemStack> getKit() {
        ArrayList<ItemStack> list = new ArrayList<>();
        for (CompoundTag tag : this.kit) {
            list.add(ItemStack.of(tag));
        }
        return list;
    }

    public boolean addItem(ItemStack stackIn) {
        ItemStack stack = stackIn.copy();
        List<ItemStack> newKit = new ArrayList<>();

        for (ItemStack item : this.getKit()) {
            if (!item.getItem().equals(stack.getItem())){
                newKit.add(item);
                continue;
            }
            int count = item.getCount();
            if(item.getMaxStackSize() == count){
                newKit.add(item);
                continue;
            }
            if (count + stack.getCount() >= item.getMaxStackSize()) count = item.getMaxStackSize();
            else count += stack.getCount();
            stack.setCount(stack.getCount() - (item.getMaxStackSize() - item.getCount()));
            item.setCount(count);
            Logger.log("Compressed Stack: " + item);
            newKit.add(item);
        }
        if(stack.getCount() > 0) newKit.add(stack);
        setKit(newKit);
        return true;
    }

    public boolean removeItem(ItemStack stack) {
        Item item = stack.getItem();
        int qty = stack.getCount();
        Main.getLogger().info("Removing " + qty + " " + stack.getDisplayName());
        List<ItemStack> newKit = new ArrayList<>();
        List<ItemStack> oldKit = getKit();
        for(int i = oldKit.size() -1; i > 0; i--){
            ItemStack kitItem = oldKit.get(i);
            if(kitItem.getItem().equals(item)){
                int oldQty = kitItem.getCount();
                kitItem.setCount(oldQty - qty);
                Main.getLogger().info("Removing " + oldQty + " from stack of " + stack.getDisplayName());
                qty -= oldQty;
                Main.getLogger().info(qty + " " + stack.getDisplayName() + " left to remove.");
                if(qty <= 0) break;
            }
            if(kitItem.getCount() > 0) newKit.add(kitItem);
        }
        Main.getLogger().info("There was " + qty + " that wasn't removed.");
        setKit(newKit);
        return true;
    }

    public void setKit(List<ItemStack> newKit){
        kit = new ArrayList<>();
        for(ItemStack item : newKit){
            CompoundTag tag = new CompoundTag();
            item.save(tag);
            kit.add(tag);
        }
        save();
    }

    public void clearKit() {
        this.kit.clear();
        this.save();
    }

    public void setCost(int newCost){
        if(newCost < 0) newCost = 0;
        Main.getLogger().info("Kit " + name + "'s cost was changed from " + cost + " to " + newCost);
        cost = newCost;
        save();
    }

    public int getCost(){
        return cost;
    }

    public IPermission getPerm() {
        String perm = Main.permRegistry.getPerm("kit") + ".";
        perm = perm + name.replaceAll(" ", "_");
        return new Permission(perm);
    }

    public void save() {
        Logger.log("Saving Kit: " + ModInfo.Constants.kitdir.getAbsolutePath() + this.name + ".json");
        JsonMethod.save(ModInfo.Constants.kitdir, this.name + ".json", this);
    }

    public void give(ServerPlayer player){
        boolean flag = false;
        for (ItemStack stack : getKit()) {
            if(!player.addItem(stack)){
                flag = true;
                player.spawnAtLocation(stack, 1.0f);
            }
        }
        if(flag) Main.textUtils.err(player, Msgs.dropped_kit.get(name));
    }

    public void setDefault(boolean flag){ isDefault = flag; save(); }
    public boolean isDefault(){ return isDefault; }
}

