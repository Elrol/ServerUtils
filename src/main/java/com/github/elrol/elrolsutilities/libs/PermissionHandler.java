package com.github.elrol.elrolsutilities.libs;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.perms.IPermissionHandler;
import com.github.elrol.elrolsutilities.api.perms.IPermission;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Map;
import java.util.UUID;

public class PermissionHandler implements IPermissionHandler {

    public boolean hasPermission(UUID uuid, String perm) {
        ServerPlayerEntity player = Methods.getPlayerFromUUID(uuid);
        return hasPermission(player, perm);
    }

    public boolean hasPermission(ServerPlayerEntity player, String perm) {
        return hasPermission(player.createCommandSourceStack(), perm);
    }

    public boolean hasPermission(CommandSource source, IPermission perm) {
        return hasPermission(source, perm.get());
    }

    public boolean hasPermission(CommandSource source, String perm) {
        try {
            PlayerEntity player = source.getPlayerOrException();

            if(player instanceof FakePlayer) return true;

            PlayerData data = Main.database.get(player.getUUID());
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
        Logger.log(filtered.toString());
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

}
