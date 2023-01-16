package dev.elrol.serverutilities.init;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.data.IRank;
import dev.elrol.serverutilities.data.Rank;
import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.ModInfo;
import dev.elrol.serverutilities.libs.Permissions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.util.*;

public class Ranks {
    public static Rank default_rank = new Rank("default", "&8[&7Player&8]", "", Permissions.defaultRankPerms(), new ArrayList<>(), 0);
    public static Rank op_rank = new Rank("op", "&8[&4Op&8]", "", Collections.singletonList("*"), new ArrayList<>(), 1000);
    public static Map<String, Rank> rankMap;

    public static void init() {
        rankMap = new HashMap<>();
        load();
        if(rankMap.isEmpty()){
            Logger.log("Rank map was empty, setting new ranks");
            rankMap.put(default_rank.getName(), default_rank);
            rankMap.put(op_rank.getName(), op_rank);
        }
        saveAll();
    }

    public static void save(Rank rank) {
        JsonMethod.save(ModInfo.Constants.rankdir, rank.getName() + ".json", rank);
        Logger.log(rank.toString());
    }

    public static void saveAll() {
        for (Map.Entry<String, Rank> entry : rankMap.entrySet()) {
            Ranks.save(entry.getValue());
            Logger.log("Saving rank: " + entry.getKey());
        }
    }

    public static void load() {
        rankMap.clear();
        if (!ModInfo.Constants.rankdir.exists()) {
            ModInfo.Constants.rankdir.mkdir();
        }
        for (File rankFile : Objects.requireNonNull(ModInfo.Constants.rankdir.listFiles())) {
            Rank rank = JsonMethod.load(ModInfo.Constants.rankdir, rankFile.getName(), Rank.class);
            if (rank == null) {
                Logger.err("Invalid File located at: " + rankFile.getAbsolutePath());
                continue;
            }
            if (rankMap.containsKey(rank.getName())) {
                Logger.err("Updating rank: " + rank.getName());
                rankMap.replace(rank.getName(), rank);
                continue;
            }
            rankMap.put(rank.getName(), rank);
            Logger.log("Loaded rank: " + rank.getName());
            Logger.log(rank.toString());
        }
        rankMap.forEach(
            (String name, Rank rank) -> { Ranks.refreshTabDisplayForRank(rank); }
        );
    }

    public static void add(Rank rank) {
        if (!rankMap.containsKey(rank.getName())) {
            rankMap.put(rank.getName(), rank);
            Ranks.save(rank);
        }
    }

    public static void remove(Rank rank) {
        if (rankMap.containsKey(rank.getName())) {
            for (File file : Objects.requireNonNull(ModInfo.Constants.rankdir.listFiles())) {
                if (!file.getName().equals(rank.getName() + ".json")) continue;
                file.delete();
                Logger.log("Rank deleted: " + rank.getName());
            }
        }
    }

    public static Rank get(String rank) {
        return rankMap.getOrDefault(rank, null);
    }

    public static void refreshTabDisplayForRank(IRank rank)
    {
        Main.database.getDatabase().forEach(
                (UUID uuid, IPlayerData playerData) -> {
                    if(playerData.getRankList().contains(rank)) {
                        ServerPlayer player = Main.mcServer.getPlayerList().getPlayer(uuid);
                        if (player != null) player.refreshTabListName();
                    }
                }
        );
    }

    public static void sendTabDisplaysToPlayer(ServerPlayer player)
    {
        player.getServer().getPlayerList().getPlayers().forEach(
            (ServerPlayer otherPlayer) -> player.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, otherPlayer))
        );
    }
}

