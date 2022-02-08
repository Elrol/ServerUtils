package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.data.ClaimBlock;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkHandler {

    public static List<ChunkPos> loadedChunks = new ArrayList<>();
    private final List<UUID> tempList = new ArrayList<>();

    @SubscribeEvent
    public void enterChunkEvent(PlayerEvent.EnteringChunk event){
        if(!(event.getEntity() instanceof ServerPlayerEntity)) return;
        if(tempList.contains(event.getEntity().getUUID())){
            tempList.remove(event.getEntity().getUUID());
            return;
        }
        ResourceLocation dim = event.getEntity().level.dimension().location();
        ClaimBlock oldPos = new ClaimBlock(dim, new ChunkPos(event.getOldChunkX(), event.getOldChunkZ()));
        ClaimBlock newPos = new ClaimBlock(dim, new ChunkPos(event.getNewChunkX(), event.getNewChunkZ()));
        UUID oldOwner = Main.serverData.getOwner(oldPos);
        UUID newOwner = Main.serverData.getOwner(newPos);
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        if(player == null) return;
        if(newOwner != null) {
            IPlayerData newData = Main.database.get(newOwner);
            //!newData.getFlag(ClaimFlagKeys.allow_entry) && !newOwner.equals(player.getUUID()) && !newData.isTrusted(player.getUUID())
            if(newData == null) {
                Logger.err("newData was null.");
            } else {
                if(!newOwner.equals(player.getUUID())) {
                    if(!newData.isTrusted(player.getUUID()) && !IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.getUUID(), "*")) {
                        tempList.add(player.getUUID());
                        TextUtils.err(player, Errs.no_entry(Methods.getDisplayName(newOwner)));
                        double x = player.blockPosition().getX() + (2 * (event.getOldChunkX() - event.getNewChunkX()));
                        double z = player.blockPosition().getZ() + (2 * (event.getOldChunkZ() - event.getNewChunkZ()));
                        BlockPos prevLoc = new BlockPos(x, player.blockPosition().getY(), z);
                        Methods.teleport(player, new Location(dim, prevLoc, player.yRot, player.xRot));

                        return;
                    }
                }
            }
            if (oldOwner != null) {
                if (!newOwner.equals(oldOwner)) {
                    if(newOwner.equals(player.getUUID())) {
                        TextUtils.msg(player, Msgs.enter_exit_claim(Methods.getDisplayName(oldOwner) + "'s", "your own"));
                    } else if(oldOwner.equals(player.getUUID())){
                        TextUtils.msg(player, Msgs.enter_exit_claim("your own", Methods.getDisplayName(newOwner) + "'s"));
                    } else {
                        TextUtils.msg(player, Msgs.enter_exit_claim(Methods.getDisplayName(oldOwner) + "'s", Methods.getDisplayName(newOwner) + "'s"));
                    }
                }
            } else {
                if(newOwner.equals(player.getUUID()))
                    TextUtils.msg(player, Msgs.enter_claim("your own"));
                else
                    TextUtils.msg(player, Msgs.enter_claim(Methods.getDisplayName(newOwner)));
            }
        } else {
            if (oldOwner != null) {
                if(oldOwner.equals(player.getUUID()))
                    TextUtils.msg(player, Msgs.exit_claim("your own"));
                else
                    TextUtils.msg(player, Msgs.exit_claim(Methods.getDisplayName(oldOwner)));
            }
        }
    }

    @SubscribeEvent
    public void onLoad(ChunkEvent.Load event){
        loadedChunks.add(event.getChunk().getPos());
    }

    @SubscribeEvent
    public void onUnload(ChunkEvent.Unload event){
        loadedChunks.remove(event.getChunk().getPos());
    }

}
