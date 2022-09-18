package dev.elrol.serverutilities.econ.averon;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.api.econ.AbstractShop;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class AveronShop extends AbstractShop {

    public AveronShop() {
        super("Averon");
    }

    @Override
    public boolean link(ServerPlayer player, Location signLoc, Location linkLoc) {
        Level world = signLoc.getLevelObj();
        BlockPos pos = signLoc.getBlockPos();
        BlockEntity te = world.getBlockEntity(pos);
        if(!(te instanceof SignBlockEntity)) return false;

        if(canEdit(player)) {
            setLinkLoc(linkLoc);
            updateSign((SignBlockEntity) te);
            return true;
        }

        return false;
    }

    @Override
    public boolean hitShop(ServerPlayer player) {
        return false;
    }

    @Override
    public boolean useShop(ServerPlayer player, Location loc) {
        if(!isLinked()) {
            Main.textUtils.err(player, Errs.not_linked(tag()));
            return false;
        }
        if(Main.isDev()) {
            BlockState linked = linkLoc.getLevelObj().getBlockState(linkLoc.getBlockPos());
            if(linked.getBlock().equals(Blocks.AIR)) {
                linkLoc.getLevelObj().setBlock(linkLoc.getBlockPos(), Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
            } else {
                linkLoc.getLevelObj().setBlock(linkLoc.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
            }
        }
        return false;
    }

    @Override
    public Component[] confirm() {
        return null;
    }

    @Override
    public boolean canCreate(ServerPlayer player) {
        return false;
    }

    public JsonObject toJson(){
        JsonObject obj = new JsonObject();

        if(getOwner() != null) obj.addProperty("owner", getOwner().toString());
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
