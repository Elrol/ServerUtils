package com.github.elrol.elrolsutilities.data;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.Location;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;

public class ServerData implements Serializable {
    private static final long serialVersionUID = -1071261237194140580L;
    public Location spawnPoint;
    private Map<UUID, Integer> muteMap = new HashMap<>();
    public Map<String, Location> warpMap = new HashMap<>();
    public Map<String, JailData> jailMap = new HashMap<>();
    public transient List<UUID> staffList = new ArrayList<>();
    public List<String> permissions = new ArrayList<>();

    private String motd = "";
    private Map<String, String> claimMap = new HashMap<>();

    public void setMOTD(String motd) {
        this.motd = motd;
        save();
    }

    public String getMotd() {
        return TextUtils.format(motd);
    }

    public boolean isClaimed(ClaimBlock claim){
        String key = claim.toString();
        return claimMap.containsKey(key);
    }

    public UUID getOwner(ClaimBlock claim){
        String key = claim.toString();
        if(claimMap.containsKey(key))
            return UUID.fromString(claimMap.get(key));
        return null;
    }

    public JailData getJail(String name) {
        return jailMap.getOrDefault(name, null);
    }

    public Location getJailCell(String name, int cell) {
        JailData jail = getJail(name);
        if(jail == null) return null;
        if(jail.cells.size() > cell) return jail.cells.get(cell);
        return null;
    }

    public void createJail(String name) {
        if(jailMap.containsKey(name)) {
            Main.getLogger().error("Jail already exists: " + name);
        } else {
            Main.getLogger().info("Jail created: " + name);
            jailMap.put(name, new JailData());
            save();
        }
    }

    public void deleteJail(String name) {
        if(!jailMap.containsKey(name)) {
            Main.getLogger().error("Jail doesn't exists: " + name);
        } else {
            Main.getLogger().info("Jail delete: " + name);
            jailMap.remove(name);
            save();
        }
    }

    public void addJailCell(String name, Location loc) {
        JailData data = getJail(name);
        if(data == null) {
            Main.getLogger().error("The " + name +  " Jail doesn't exist");
            return;
        }
        data.cells.add(loc);
        Main.getLogger().info("Cell added to the " + name + "Jail");
        save();
    }

    public void removeJailCell(String name, int cell) {
        JailData data = getJail(name);
        if(data == null) {
            Main.getLogger().error("Jail doesn't exist: " + name);
            return;
        }
        data.cells.remove(cell);
        Main.getLogger().info("Cell #" + cell + "removed from " + name + " Jail");
        save();
    }

    public void jail(ServerPlayerEntity player, String name, int cell, int min) {
        JailData jail = getJail(name);
        if(jail == null) {
            Main.getLogger().error("Jail doesn't exist: " + name);
            return;
        }
        if(jail.cells.size() <= cell) {
            Main.getLogger().error("Jail cell doesn't exist: " + name);
            return;
        }
        PlayerData data = Main.database.get(player.getUUID());
        data.jail(name, cell, min);
        Methods.teleport(player, jail.cells.get(cell));
        save();
    }

    public void claim(ClaimBlock claim, UUID uuid){
        claimMap.put(claim.toString(), uuid.toString());
        save();
    }

    public void unclaim(ClaimBlock claim){
        claimMap.remove(claim.toString());
        save();
    }

    public void unclaimAll(ServerPlayerEntity player) {
        List<String> keys = new ArrayList<>();
        claimMap.forEach((key, uuid) -> {
            if(uuid.equals(player.getUUID().toString())) keys.add(key);
        });
        keys.forEach(key -> claimMap.remove(key));
        save();
    }

    public void setSpawn(Location loc) {
        this.spawnPoint = loc;
        this.save();
    }

    public void addWarp(String warp, Location loc) {
        if (this.warpMap.containsKey(warp = warp.toLowerCase())) {
            this.warpMap.replace(warp, loc);
        } else {
            this.warpMap.put(warp, loc);
        }
        this.save();
    }

    public void delWarp(String warp) {
        if (this.warpMap.containsKey(warp = warp.toLowerCase())) {
            this.warpMap.remove(warp);
            this.save();
        }
    }

    public Location getWarp(String warp) {
        if (this.warpMap.containsKey(warp = warp.toLowerCase())) {
            return this.warpMap.get(warp);
        }
        return null;
    }

    public Set<String> getWarpNames() {
        return this.warpMap.keySet();
    }

    public void updateAllPlayers() {
        List<ServerPlayerEntity> list = Main.mcServer.getPlayerList().getPlayers();
        for (ServerPlayerEntity player : list) {
            PlayerData data = Main.database.get(player.getUUID());
            data.update();
        }
    }

    public void updateAllPlayersWithRank(String rank) {
        MinecraftServer server = Main.mcServer;
        PlayerList playerList = server.getPlayerList();
        List<ServerPlayerEntity> list = playerList.getPlayers();
        for (ServerPlayerEntity player : list) {
            PlayerData data = Main.database.get(player.getUUID());
            if (!data.getRanks().contains(rank)) continue;
            data.update();
        }
        playerList.saveAll();
        playerList.reloadResources();
    }

    public void mutePlayer(ServerPlayerEntity player, Integer min) {
        if (this.muteMap.containsKey(player.getUUID())) {
            int old = muteMap.get(player.getUUID());
            TextUtils.err(player, Errs.mute_changed("" + old + (old > 1 ? " minutes" : " minute"), "" + min + (min > 1 ? " minutes" : " minute")));
            this.muteMap.replace(player.getUUID(), min);
        } else {
            this.muteMap.put(player.getUUID(), min);
            TextUtils.err(player, Errs.muted("" + min + (min > 1 ? " minutes" : " min")));
        }
        this.save();
    }

    public void unmutePlayer(ServerPlayerEntity player) {
        if (this.muteMap.containsKey(player.getUUID())) {
            this.muteMap.remove(player.getUUID());
            TextUtils.msg(player, Msgs.unmuted());
            this.save();
        }
    }

    public void save() {
        JsonMethod.save(new File(Main.dir, "/data"), "serverdata.dat", Main.serverData);
    }

    public int getMute(UUID uuid) {
        if (this.muteMap.containsKey(uuid)) {
            int time = this.muteMap.get(uuid);
            if (time <= 0) {
                this.muteMap.remove(uuid);
                this.save();
                return 0;
            }
            return time;
        }
        return 0;
    }

    public void tickJails() {
        Main.database.getDatabase().forEach((uuid, data) -> data.tickJail());
    }

    public void tickMutes() {
        ArrayList<UUID> unmuteList = new ArrayList<>();
        this.muteMap.forEach((uuid, time) -> {
            time = time - 1;
            ServerPlayerEntity player = Methods.getPlayerFromUUID(uuid);
            if (player == null) {
                return;
            }
            if (time <= 0) {
                TextUtils.msg(player, Msgs.unmuted());
                unmuteList.add(uuid);
            } else {
                muteMap.replace(uuid, time);
                Logger.debug("Mute time for " + uuid + ": " + muteMap.get(uuid));
            }
        });
        if (!unmuteList.isEmpty()) {
            for (UUID uuid2 : unmuteList) {
                muteMap.remove(uuid2);
            }
            save();
        }
    }

    public List<ClaimBlock> getClaims(UUID uuid){
        List<ClaimBlock> claims = new ArrayList<>();
        if(claimMap.isEmpty()) return claims;
        claimMap.forEach((claim, id) -> {
            //Main.getLogger().info("Claim:" + claim + ", " + id);
            if(id.equals(uuid.toString())) claims.add(ClaimBlock.of(claim));
        });

        return claims;
    }
}