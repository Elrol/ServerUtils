package dev.elrol.serverutilities.events;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.api.enums.ClaimFlagKeys;
import dev.elrol.serverutilities.data.ClaimBlock;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import dev.elrol.serverutilities.libs.text.TextUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkHandler {

    public static List<ChunkPos> loadedChunks = new ArrayList<>();
    private final List<UUID> tempList = new ArrayList<>();

    @SubscribeEvent
    public void enterChunkEvent(EntityEvent.EnteringSection event){
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        if(tempList.contains(event.getEntity().getUUID())){
            tempList.remove(event.getEntity().getUUID());
            return;
        }
        ResourceLocation dim = event.getEntity().level.dimension().location();

        ClaimBlock oldPos = new ClaimBlock(dim, event.getOldPos().chunk());
        ClaimBlock newPos = new ClaimBlock(dim, event.getNewPos().chunk());
        UUID oldOwner = Main.serverData.getOwner(oldPos);
        UUID newOwner = Main.serverData.getOwner(newPos);
        if(newOwner != null) {
            IPlayerData newData = Main.database.get(newOwner);
            //!newData.getFlag(ClaimFlagKeys.allow_entry) && !newOwner.equals(player.getUUID()) && !newData.isTrusted(player.getUUID())
            if(newData == null) {
                Logger.err("newData was null.");
            } else {
                //If the player not the owner of the new chunk
                if(!newOwner.equals(player.getUUID())) {
                    if(!newData.isTrusted(player.getUUID()) && !IElrolAPI.getInstance().getPermissionHandler().hasPermission(player.getUUID(), "*")) {
                        if(!newData.getClaimFlags().get(ClaimFlagKeys.allow_entry)) {
                            tempList.add(player.getUUID());
                            Main.textUtils.err(player, Errs.no_entry(Methods.getDisplayName(newOwner)));
                            double x = player.blockPosition().getX() + (2 * (oldPos.getPos().x - newPos.getPos().x));
                            double z = player.blockPosition().getZ() + (2 * (oldPos.getPos().z - newPos.getPos().z));
                            BlockPos prevLoc = new BlockPos(x, player.blockPosition().getY(), z);
                            Methods.teleport(player, new Location(dim, prevLoc, player.yHeadRot, player.yHeadRotO));

                            return;
                        }
                    }
                }
            }
            if (oldOwner != null) {
                if (!newOwner.equals(oldOwner)) {
                    if(newOwner.equals(player.getUUID())) {
                        Main.textUtils.msg(player, Msgs.enter_exit_claim.get(Methods.getDisplayName(oldOwner) + "'s", "your own"));
                    } else if(oldOwner.equals(player.getUUID())){
                        Main.textUtils.msg(player, Msgs.enter_exit_claim.get("your own", Methods.getDisplayName(newOwner) + "'s"));
                    } else {
                        Main.textUtils.msg(player, Msgs.enter_exit_claim.get(Methods.getDisplayName(oldOwner) + "'s", Methods.getDisplayName(newOwner) + "'s"));
                    }
                }
            } else {
                if(newOwner.equals(player.getUUID()))
                    Main.textUtils.msg(player, Msgs.enter_claim.get("your own"));
                else
                    Main.textUtils.msg(player, Msgs.enter_claim.get(Methods.getDisplayName(newOwner)));
            }
        } else {
            if (oldOwner != null) {
                if(oldOwner.equals(player.getUUID()))
                    Main.textUtils.msg(player, Msgs.exit_claim.get("your own"));
                else
                    Main.textUtils.msg(player, Msgs.exit_claim.get(Methods.getDisplayName(oldOwner)));
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
