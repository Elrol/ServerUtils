package com.github.elrol.elrolsutilities.data;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.api.econ.IShop;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.libs.SignUtils;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.UUID;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public abstract class ChestShop implements IShop {

    protected UUID owner;
    protected final Location location;
    protected Location linkLocation;
    protected CompoundNBT item;

    public int qty;
    public float price;

    public ChestShop(UUID uuid, Location loc) {
        owner = uuid;
        loc.setYaw(0f);
        loc.setpitch(0f);
        location = loc;
    }

    public ChestShop(UUID uuid, Location loc, int qty) {
        this.owner= uuid;
        this.location = loc;
        this.qty = qty;

    }

    public ChestShop(Location loc, ItemStack item, float price) {
        this.owner = null;
        this.location = loc;
        this.qty = item.getCount();
        this.price = price;
        setItem(item);
    }

    static boolean isValidStorage(TileEntity entity) {
        return entity.getCapability(ITEM_HANDLER_CAPABILITY).isPresent();
    }

    public abstract boolean isAdmin();

    public boolean isOwner(UUID uuid) {
        if(owner == null) return false;
        return owner.equals(uuid);
    }

    public UUID getUUID() {
        return owner;
    }

    public void setUUID(@Nullable UUID uuid) {
        owner = uuid;
    }

    public SignTileEntity getSign() {
        ServerWorld world = Main.mcServer.getLevel(location.getWorld());
        if(world != null) {
            TileEntity te = world.getBlockEntity(location.getBlockPos());
            if (te instanceof SignTileEntity) {
                return (SignTileEntity) te;
            }
        }
        return null;
    }

    public void updateSign() {
        ServerWorld world = Main.mcServer.getLevel(location.getWorld());
        if(world != null) {
            BlockState signState = world.getBlockState(location.getBlockPos());
            world.sendBlockUpdated(location.getBlockPos(), signState, signState, Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    public boolean setChest(Location chestLocation) {
        linkLocation = chestLocation;
        IItemHandler handler = getItemHandler();
        if(handler != null) {
            ItemStack stack = ItemStack.EMPTY;
            for(int i = 0; i < handler.getSlots(); i++) {
                stack = handler.getStackInSlot(i).copy();
                if(!stack.isEmpty()) {
                    stack.setCount(qty);
                    setItem(stack);
                    return true;
                }
            }
            if(!stack.isEmpty() && stack.getMaxStackSize() < qty) {
                qty = stack.getMaxStackSize();
                SignTileEntity sign = getSign();
                if(sign != null) {
                    sign.setMessage(2, new StringTextComponent("" + qty));
                    updateSign();
                }
            }
        }
        return false;
    }

    public ItemStack getItem() {
        return ItemStack.of(item);
    }

    public void setItem(ItemStack stack) {
        CompoundNBT tag = new CompoundNBT();
        stack.save(tag);
        item = tag;
    }

    public IItemHandler getItemHandler() {
        if(linkLocation != null) {
            ServerWorld world = Main.mcServer.getLevel(linkLocation.getWorld());
            if(world == null) return null;
            TileEntity entity = world.getBlockEntity(linkLocation.getBlockPos());
            if(entity != null && isValidStorage(entity)) {
                return entity.getCapability(ITEM_HANDLER_CAPABILITY, null).orElse(null);
            }
        }
        return null;
    }

    @Override
    public void parseSign(SignTileEntity sign) {
        String qty = SignUtils.readLine(sign, 2);
        String cost = SignUtils.readLine(sign, 3);

        if(qty.isEmpty()) this.qty = 1;
        else this.qty = Integer.parseInt(qty);

        if(cost.isEmpty()) this.price = 0.0f;
        else this.price = Float.parseFloat(cost);
    }

    public boolean canCreate(ServerPlayerEntity player) {
        if(isAdmin()) return IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.createCommandSourceStack(), CommandConfig.chestshop_adminperm.get());
        PlayerData data = Main.database.get(player.getUUID());
        if(data.shops.size() >= data.maxShops) return false;
        double cost = FeatureConfig.chestshop_price.get();
        if(cost > data.getBal()) {
            TextUtils.err(player, Errs.afford_shop(TextUtils.parseCurrency(cost, true), TextUtils.parseCurrency(data.getBal(), true)));
            return false;
        }
        return IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.createCommandSourceStack(), Main.permRegistry.getPerm("chestshop"));
    }

    public boolean canEdit(ServerPlayerEntity player) {
        if(isAdmin()) return IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.createCommandSourceStack(), CommandConfig.chestshop_adminperm.get());
        return isOwner(player.getUUID());
    }

    @Override
    public UUID getOwner() {
        return null;
    }

    @Override
    public float getCost() {
        return 0;
    }

    @Override
    public String getTag() {
        return null;
    }

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
}
