package dev.elrol.serverutilities.api.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IRank {

    void            setName(String name);
    String          getName();
    String          getPrefix();
    void            setPrefix(String prefix);
    String          getSuffix();
    void            setSuffix(String suffix);
    void            setWeight(int weight);
    int             getWeight();
    boolean         addPerm(String perm);
    boolean         removePerm(String perm);
    List<String>    getPerms();
    void            setPerms(List<String> permList);
    void            addParent(String parent);
    void            removeParent(String parent);
    List<IRank>     getParents();
    long            getRankUp();
    void            setRank_up(int min);
    float           getRankUpCost();
    void            setRankUpCost(float cost);
    List<String>    getNextRanks();
    void            addNextRank(String rank);
    void            removeNextRank(String rank);
    void            clearCmds();
    void            addCmd(String cmd);
    void            removeCmd(int index);
    String          getCmd(int index);
    void            runCmds(UUID uuid);
    List<String>    getCmds();
    void            addDiscordID(long serverID, long roleID);
    Map<Long,Long>  getDiscordIDs();
}
