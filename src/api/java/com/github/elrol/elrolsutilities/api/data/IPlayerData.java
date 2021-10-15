package com.github.elrol.elrolsutilities.api.data;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IPlayerData {

    void                update();
    void                save();

    int                 getMinPlayed();
    String              getTimePlayed();
    Long                tillUseKit(IKit kit);

    void                jail(String jail, int cell, int min);
    long                getJailTime();
    boolean             isJailed();

    boolean             isStaff();
    void                toggleBypass();

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
    ServerPlayerEntity  getLastMsg();
    String              getDisplayName();
}
