package dev.elrol.serverutilities.api.econ;

import com.google.gson.JsonObject;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.data.IPlayerDatabase;
import dev.elrol.serverutilities.api.data.Location;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.text.DecimalFormat;
import java.util.UUID;

public abstract class AbstractShop {

    protected float cost = 0.0f;
    private UUID owner;
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
    public abstract boolean link(ServerPlayer player, Location signLoc, Location linkLoc);

    /**
     * Called when the sign is hit. Returns true if allowed to break and false if not.
     * @return boolean
     */
    public abstract boolean hitShop(ServerPlayer player);

    /**
     * Called when the sign is used. Returns true if allowed to use and false if not.
     * @return boolean
     */
    public boolean useShop(ServerPlayer player, Location loc) {
        if(canCreate(player)) {
            IPlayerDatabase database = IElrolAPI.getInstance().getPlayerDatabase();
            IPlayerData data = database.get(player.getUUID());
            return data.charge(cost);
        }
        return false;
    }

    public abstract Component[] confirm();

    public abstract boolean canCreate(ServerPlayer player);

    public int maxShops(String perm, ServerPlayer player) {
        IPlayerData data = IElrolAPI.getInstance().getPlayerDatabase().get(player.getUUID());
        int max = 0;
        for(String s : data.getPerms()) {
            if(s != null && s.startsWith(perm)) {
                int i = Integer.parseInt(s.replace(perm, ""));
                if(max < i ){
                    max = i;
                }
            }
        }
        return max;
    }

    public boolean canEdit(ServerPlayer player) {
        if(owner == null) {
            setOwner(player.getUUID());
            return true;
        }
        return player.getUUID().equals(owner);
    }

    public void updateSign(SignBlockEntity sign){
        Level world = sign.getLevel();
        BlockPos pos = sign.getBlockPos();

        if(world == null) return;

        String textTag = (isLinked() ? ChatFormatting.GREEN : ChatFormatting.RED) + "[" + tag() + "]";
        sign.setMessage(0, Component.literal(textTag));

        DecimalFormat df = new DecimalFormat("0.00");

        String textCost = "$" + df.format(getCost());
        sign.setMessage(3, Component.literal(textCost));
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
