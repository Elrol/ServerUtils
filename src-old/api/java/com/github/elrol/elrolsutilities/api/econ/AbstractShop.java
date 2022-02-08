package com.github.elrol.elrolsutilities.api.econ;

import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.api.data.IPlayerDatabase;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.UUID;

public abstract class AbstractShop {

    protected float cost = 0.0f;
    protected UUID owner;
    protected Location linkLoc;
    private transient final String tag;

    public AbstractShop(String tag) {
        this.tag = tag;
    }

    public AbstractShop(String tag, UUID owner) {
        this.tag = tag;
        this.owner = owner;
    }

    /**
     * @return Owner's Unique ID
     */
    public UUID getOwner() { return owner; }
    public void setOwner(UUID uuid) { this.owner = uuid; }

    /**
     * @return The cost to use the shop
     */
    public float getCost() { return cost; }
    public void setCost(float cost) { this.cost = cost; }

    /**
     * @return Location of the linked block
     */
    public Location getLinkLoc() { return linkLoc; }
    public void setLinkLoc(Location linkLoc) { this.linkLoc = linkLoc; }

    /**
     * Called when the sign is linked. Returns true if link was successful, false if not.
     * */
    public abstract boolean link(ServerPlayerEntity player, Location signLoc, Location linkLoc);

    /**
     * Called when the sign is hit. Returns true if allowed to break and false if not.
     * @return boolean
     */
    public abstract boolean hitShop(ServerPlayerEntity player);

    /**
     * Called when the sign is used. Returns true if allowed to use and false if not.
     * @return boolean
     */
    public boolean useShop(ServerPlayerEntity player, Location signLoc) {
        if(canCreate(player)) {
            IPlayerDatabase database = IElrolAPI.getInstance().getPlayerDatabase();
            IPlayerData data = database.get(player.getUUID());
            return data.charge(cost);
        }
        return false;
    }

    public abstract ITextComponent[] confirm();

    public abstract boolean canCreate(ServerPlayerEntity player);

    public int maxShops(String perm, ServerPlayerEntity player) {
        IPlayerData data = IElrolAPI.getInstance().getPlayerDatabase().get(player.getUUID());
        int max = 0;
        for(String s : data.getPerms()) {
            if(s.startsWith(perm)) {
                int i = Integer.parseInt(s.replace(perm, ""));
                if(max < i ){
                    max = i;
                }
            }
        }
        return max;
    }

    public boolean canEdit(ServerPlayerEntity player) {
        if(owner == null) {
            owner = player.getUUID();
            return true;
        }
        return player.getUUID().equals(owner);
    }

    public void updateSign(SignTileEntity sign){
        World world = sign.getLevel();
        BlockPos pos = sign.getBlockPos();

        if(world == null) return;

        String textTag = (isLinked() ? TextFormatting.GREEN : TextFormatting.RED) + "[" + tag() + "]";
        sign.setMessage(0, new StringTextComponent(textTag));

        DecimalFormat df = new DecimalFormat("0.00");

        String textCost = "$" + df.format(getCost());
        sign.setMessage(3, new StringTextComponent(textCost));
        sign.setChanged();

        BlockState state = world.getBlockState(pos);
        world.sendBlockUpdated(pos, state, state, 3);
    }

    public boolean isAdmin() { return false; }
    public boolean isLinked() { return linkLoc != null; }
    public String tag() { return tag; }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("owner", owner.toString());
        obj.addProperty("cost", cost);
        obj.addProperty("linkLoc", isLinked() ? linkLoc.toString() : "");
        return obj;
    }
}
