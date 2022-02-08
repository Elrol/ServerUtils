package com.github.elrol.elrolsutilities.econ.averon;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.api.econ.AbstractShop;
import com.github.elrol.elrolsutilities.econ.chestshop.ChestShopManager;
import com.github.elrol.elrolsutilities.econ.chestshop.ChestShopType;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.UUID;

public class AveronShop extends AbstractShop {
    private static final String perm = "shop.averon.create";

    public AveronShop() {
        super("Averon");
    }

    @Override
    public boolean link(ServerPlayerEntity player, Location signLoc, Location linkLoc) {
        World world = signLoc.getWorldObj();
        BlockPos pos = signLoc.getBlockPos();
        TileEntity te = world.getBlockEntity(pos);
        if(!(te instanceof SignTileEntity)) return false;

        if(canEdit(player)) {
            setLinkLoc(linkLoc);
            updateSign((SignTileEntity) te);
            return true;
        }

        return false;
    }

    @Override
    public boolean hitShop(ServerPlayerEntity player) {
        return false;
    }

    @Override
    public boolean useShop(ServerPlayerEntity player, Location signLoc) {
        if(!isLinked()) {
            TextUtils.err(player, Errs.not_linked(tag()));
            return false;
        }
        if(super.useShop(player, signLoc)) {
            BlockState linked = linkLoc.getWorldObj().getBlockState(linkLoc.getBlockPos());
            if(linked.getBlock().equals(Blocks.AIR)) {
                linkLoc.getWorldObj().setBlock(linkLoc.getBlockPos(), Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
            } else {
                linkLoc.getWorldObj().setBlock(linkLoc.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
            }
        }
        return false;
    }

    @Override
    public ITextComponent[] confirm() {
        return null;
    }

    @Override
    public boolean canCreate(ServerPlayerEntity player) {
        if(canEdit(player)) {
            IPlayerData data = Main.database.get(player.getUUID());
            int shops = data.getShops().size();
            int max = maxShops(ChestShopManager.maxShops, player);
            if(shops >= max) {
                TextUtils.err(player, Errs.max_shops());
                return false;
            }
            return IElrolAPI.getInstance().getPermissionHandler().hasPermission(player, perm);
        }
        return false;
    }

    public JsonObject toJson(){
        JsonObject obj = new JsonObject();

        if(owner != null) obj.addProperty("owner", owner.toString());
        if(cost > 0) obj.addProperty("cost", cost);
        if(isLinked()) obj.addProperty("linkLoc", linkLoc.toString());

        return obj;
    }

    public static AveronShop fromJson(JsonObject obj) {
        AveronShop shop = new AveronShop();

        if(obj.has("owner"))
            shop.setOwner(UUID.fromString(obj.get("owner").getAsString()));
        if(obj.has("cost"))
            shop.setCost(obj.get("cost").getAsFloat());
        if(obj.has("linkLoc"))
            shop.setLinkLoc(Location.fromString(obj.get("linkLoc").getAsString()));
        return shop;
    }
}
