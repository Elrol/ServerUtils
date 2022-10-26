package dev.elrol.serverutilities.libs;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.enums.ClaimFlagKeys;
import dev.elrol.serverutilities.api.perms.IPermission;
import dev.elrol.serverutilities.api.perms.IPermissionHandler;
import dev.elrol.serverutilities.data.ClaimBlock;
import dev.elrol.serverutilities.libs.text.Errs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Map;
import java.util.UUID;

public class PermissionHandler implements IPermissionHandler {

    public void addPermission(String perm) {
        Main.permRegistry.add(perm);
    }

    public boolean hasPermission(UUID uuid, String perm) {
        IPlayerData data = Main.database.get(uuid);
        if(data.hasPerm("*")) return true;
        return data.hasPerm(perm);
    }

    public boolean hasPermission(ServerPlayer player, String perm) {
        return hasPermission(player.createCommandSourceStack(), perm);
    }

    public boolean hasPermission(CommandSourceStack source, IPermission perm) {
        return hasPermission(source, perm.get());
    }

    public boolean hasPermission(CommandSourceStack source, String perm) {
        try {
            ServerPlayer player = source.getPlayerOrException();

            if(player instanceof FakePlayer) return true;

            return hasPermission(player.getUUID(), perm);
        }
        catch (CommandSyntaxException e) {
            return true;
        }
    }

    public boolean hasPermission(CommandSourceStack source, String node, String perm){
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

    public boolean hasChunkPermission(ServerPlayer player, BlockPos pos) {
        IPlayerData pdata = Main.database.get(player.getUUID());
        if(pdata.isJailed()) {
            Main.textUtils.err(player.createCommandSourceStack(), Errs.jailed((int)pdata.getJailTime()));
            return false;
        }
        ResourceLocation dim = player.level.dimension().location();
        ClaimBlock claim = new ClaimBlock(dim, pos);
        if(Main.serverData.isClaimed(claim) && !Main.serverData.getOwner(claim).equals(player.getUUID())){
            UUID owner = Main.serverData.getOwner(claim);
            IPlayerData data = Main.database.get(owner);
            if(!data.getFlag(ClaimFlagKeys.allow_switch) || !data.isTrusted(player.getUUID())) {
                Main.textUtils.err(player, Errs.chunk_claimed(data.getDisplayName()));
                return false;
            }
        }
        return true;
    }

}
