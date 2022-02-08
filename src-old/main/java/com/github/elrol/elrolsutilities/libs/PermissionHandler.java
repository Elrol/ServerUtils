package com.github.elrol.elrolsutilities.libs;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.api.enums.ClaimFlagKeys;
import com.github.elrol.elrolsutilities.api.perms.IPermission;
import com.github.elrol.elrolsutilities.api.perms.IPermissionHandler;
import com.github.elrol.elrolsutilities.data.ClaimBlock;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Map;
import java.util.UUID;

public class PermissionHandler implements IPermissionHandler {

    public boolean hasPermission(UUID uuid, String perm) {
        ServerPlayerEntity player = Methods.getPlayerFromUUID(uuid);
        return hasPermission(player, perm);
    }

    public boolean hasPermission(ServerPlayerEntity player, String perm) {
        return hasPermission(player.createCommandSource(), perm);
    }

    public boolean hasPermission(CommandSource source, IPermission perm) {
        return hasPermission(source, perm.get());
    }

    public boolean hasPermission(CommandSource source, String perm) {
        try {
            PlayerEntity player = source.getPlayerOrException();

            if(player instanceof FakePlayer) return true;

            IPlayerData data = Main.database.get(player.getUUID());
            if(data.hasPerm("*")) return true;
            if(data.hasPerm(perm)) {
                return true;
            } else {
                //Main.getLogger().debug(source.getDisplayName().getString() + " does not have the '" + perm + "' permission");
                return false;
            }
        }
        catch (CommandSyntaxException e) {
            return true;
        }
    }

    public boolean hasPermission(CommandSource source, String node, String perm){
        boolean hasPerm = false;
        int i = 1;
        Map<String, String> filtered = Main.permRegistry.filterPerms(node);
        for(Map.Entry<String, String> entry : filtered.entrySet()){
            if(hasPermission(source, entry.getValue())){
                hasPerm = true;
                //Logger.log("[" + i + "]Player can use childnode: " + entry.getKey());
                break;
            } else {
                //Logger.log("[" + i + "]Player can't use childnode: " + entry.getKey());
            }
            i++;
        }
        if(hasPerm) return true;
        //Logger.log("1134611-Checking other perm");
        return hasPermission(source, perm);
    }

    public boolean hasChunkPermission(ServerPlayerEntity player, BlockPos pos) {
        IPlayerData pdata = Main.database.get(player.getUUID());
        if(pdata.isJailed()) {
            TextUtils.err(player.createCommandSource(), Errs.jailed((int)pdata.getJailTime()));
            return false;
        }
        ResourceLocation dim = player.level.dimension().getRegistryName();
        ClaimBlock claim = new ClaimBlock(dim, pos);
        if(Main.serverData.isClaimed(claim) && !Main.serverData.getOwner(claim).equals(player.getUUID())){
            UUID owner = Main.serverData.getOwner(claim);
            IPlayerData data = Main.database.get(owner);
            if(!data.getFlag(ClaimFlagKeys.allow_switch) || !data.isTrusted(player.getUUID())) {
                TextUtils.err(player, Errs.chunk_claimed(data.getDisplayName()));
                return false;
            }
        }
        return true;
    }

}
