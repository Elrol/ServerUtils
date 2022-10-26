package dev.elrol.serverutilities.api.data;

import dev.elrol.serverutilities.api.enums.ClaimFlagKeys;
import net.minecraft.server.level.ServerPlayer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IPlayerData {

    UUID                getUUID();
    void                update();
    void                save();

    int                 getMinPlayed();
    String              getTimePlayed();
    Long                tillUseKit(IKit kit);

    void                jail(String jail, int cell, int min);
    void                unjail();
    long                getJailTime();
    boolean             isJailed();
    String              getJail();
    int                 getJailCell();

    boolean             isStaff();
    void                toggleBypass();
    boolean             canBypass();

    boolean             charge(double cost);
    void                pay(double amount);
    double              getBal();
    void                setBal(double newBal);

    List<String>        getPerms();
    void                addPerm(String p);
    void                removePerm(String p);
    void                clearPerms();
    void                checkPerms();
    boolean             hasPerm(String perm);
    boolean             hasPermOrOp(String perm);

    List<String>        getRanks();
    String              getStringRanks();
    List<IRank>         getRankList();
    void                addRank(IRank rank);
    void                setRank(IRank rank);
    void                removeRank(String rank);
    IRank               getDomRank();

    boolean             isTrusted(UUID uuid);
    void                trust(UUID uuid);
    void                untrust(UUID uuid);

    Set<String>         getHomeNames();
    String              getHome(String name);
    void                addHome(String name, Location loc);
    void                delHome(String name);

    String              getPrefix();
    String              getSuffix();
    ServerPlayer        getLastMsg();
    void                setLastMsg(UUID uuid);
    String              getDisplayName();

    void                setFly(boolean flag);
    boolean             canFly();

    void                setFlying(boolean flag);
    boolean             isFlying();

    void                setGodmode(boolean flag);
    boolean             hasGodmode();

    boolean             canRankUp();
    void                allowRankUp(boolean flag);

    long                timeTillNextRank();
    void                setTimeTillNextRank(long time);

    long                timeLastOnline();
    List<Location>      getShops();
    void                addShop(Location loc);
    void                removeShop(Location loc);

    Location            getPrevLoc();
    boolean             msgDisabled();
    int                 getMaxClaims();
    int                 getMaxHomes();
    int                 getMaxShops();
    void                setFlag(ClaimFlagKeys key, Boolean flag);
    boolean             getFlag(ClaimFlagKeys key);
    ITpRequest          getTpRequest();
    void                setTpRequest(ITpRequest tpRequest);
    void                setMsgDisabled(boolean flag);
    void                setNickname(String name);
    String              getNickname();
    boolean             hasBeenWarned();
    void                setHasBeenWarned(boolean flag);
    void                toggleStaffChat();
    boolean             usingStaffChat();
    LocalDateTime       getFirstJoin();
    void                setUsername(String name);
    String              getUsername();
    boolean             gotFirstKit();
    void                gotFirstKit(boolean flag);
    void                setLastOnline(long time);

    boolean             isPatreon();
    void                setPatreon(boolean flag);

    void                setPrevLoc(Location loc);

    Map<String, Integer>            getKitCooldowns();
    Map<String, Location>           getHomes();
    Map<ClaimFlagKeys, Boolean>     getClaimFlags();

    // Discord things
    long                getDiscordID();
    void                setDiscordID(long id);

    String              getTitle();
    void                setTitle(String title);

    void                tickJail();
    int                 getVoteRewardCount();
    void                addVoteReward();
    void                clearVoteReward();
}
