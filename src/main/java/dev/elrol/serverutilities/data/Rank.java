package dev.elrol.serverutilities.data;

import com.mojang.authlib.GameProfile;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.data.IRank;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.Methods;

import java.util.*;

public class Rank implements IRank {
    private String name;
    private String prefix;
    private String suffix;
    public List<String> perms;
    public List<String> parents;
    public List<String> cmds;
    private int weight;

    private int rank_up;
    private List<String> next_ranks;
    private float rank_up_cost;

    private Map<Long,Long> discordIds = new HashMap<>();

    public Rank(String name) {
        this.name = name;
        prefix = "";
        suffix = "";
        perms = new ArrayList<>();
        parents = new ArrayList<>();
        next_ranks = new ArrayList<>();
        cmds = new ArrayList<>();
        weight = 0;
        Ranks.save(this);
    }

    public Rank(String name, String prefix, String suffix, List<String> perms, List<String> parents, int weight){
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.perms = perms;
        this.parents = parents;
        this.weight = weight;
        next_ranks = new ArrayList<>();
        cmds = new ArrayList<>();
        rank_up = 0;
    }

    public void setName(String name){
        this.name = name;
        Ranks.save(this);
    }

    public String getName(){ return name; }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
        Ranks.save(this);
    }

    public String getSuffix() {
        if(suffix == null) setSuffix("");
        return suffix;
    }

    public void setSuffix(String suffix){
        this.suffix = suffix;
        Ranks.save(this);
    }

    public void setWeight(int weight){
        this.weight = weight;
        Ranks.save(this);
    }

    public int getWeight() { return weight; }

    public boolean addPerm(String perm) {
        if (this.perms.contains(perm)) {
            return false;
        }
        this.perms.add(perm);
        Main.mcServer.getPlayerList().getPlayers().forEach(player -> {
            IPlayerData data = Main.database.get(player.getUUID());
            if(!data.getRankList().contains(this)) return;
            data.checkPerms();
        });
        Ranks.save(this);
        return true;
    }

    public boolean removePerm(String perm) {
        if (this.perms.contains(perm)) {
            this.perms.remove(perm);
            Ranks.save(this);
            return true;
        }
        return false;
    }

    public List<String> getPerms() {
        List<String> allPerm = new ArrayList<>(perms);
        parents.forEach(parent -> {
            if(Ranks.rankMap.containsKey(parent))
            Ranks.rankMap.get(parent).getPerms().forEach(perm -> {
                if(!allPerm.contains(perm)) allPerm.add(perm);
            });
        });
        return allPerm;
    }

    public void setPerms(List<String> permList) {
        this.perms = permList;
        Ranks.save(this);
    }

    public void addParent(String parent){
        if(parents.contains(parent)) return;
        parents.add(parent);
        Ranks.save(this);
    }

    public void removeParent(String parent){
        parents.remove(parent);
        Ranks.save(this);
    }

    public List<IRank> getParents(){
        List<IRank> p = new ArrayList<>();
        for(String parent : parents){
            if(Ranks.rankMap.containsKey(parent))
                p.add(Ranks.rankMap.get(parent));
        }
        return p;
    }

    public long getRankUp() { return rank_up * 60000L; }
    public void setRank_up(int min){
        if(min < 0) min = 0;
        rank_up = min;
        Main.getLogger().info("Rank " + name + "'s rank-up time has been set to " + min + " min(s)");
    }

    public float getRankUpCost() { return rank_up_cost; }

    public void setRankUpCost(float cost) {
        rank_up_cost = cost > 0 ? cost : 0;
    }

    public List<String> getNextRanks() {
        if(next_ranks == null){
            next_ranks = new ArrayList<>();
            Ranks.save(this);
        }
        return next_ranks;
    }
    public void addNextRank(String rank){
        if(next_ranks.contains(rank)){
            Main.getLogger().error("The " + name + " rank already has the " + rank + " as a next rank.");
        } else {
            next_ranks.add(rank);
            Ranks.save(this);
        }
    }
    public void removeNextRank(String rank){
        if(next_ranks.contains(rank)){
            next_ranks.remove(rank);
            Ranks.save(this);
        } else {
            Main.getLogger().error("The " + name + " rank doesn't have the " + rank + " as a next rank.");
        }
    }

    public void clearCmds() {
        cmds.clear();
        Ranks.save(this);
    }

    public void addCmd(String cmd) {
        if(cmds == null) cmds = new ArrayList<>();
        cmds.add(cmd);
        Ranks.save(this);
    }

    public void removeCmd(int index) {
        if(cmds != null)
            cmds.remove(index);
        Ranks.save(this);
    }

    public String getCmd(int index) {
        if(cmds.size() <= index) return "";
        return cmds.get(index);
    }

    public void runCmds(UUID uuid) {
        Optional<GameProfile> profile = Methods.getPlayerCachedProfile(uuid);
        profile.ifPresent(p -> {
            for(String cmd : cmds) {
                Methods.runCommand(cmd.replace("{player}", p.getName()));
            }
        });
    }

    public List<String> getCmds() {
        return cmds;
    }

    public void addDiscordID(long serverID, long roleID) {
        if(discordIds == null) discordIds = new HashMap<>();
        discordIds.put(serverID, roleID);
    }

    @Override
    public Map<Long, Long> getDiscordIDs() {
        if(discordIds == null) discordIds = new HashMap<>();
        return discordIds;
    }

    public String toString(){
        StringBuilder s = new StringBuilder("Rank[");
        s.append("name:").append(name).append(",");
        s.append("prefix:").append(prefix).append(",");
        s.append("suffix:").append(suffix).append(",");
        s.append("perms:{");
        for(String p : perms){
            if(s.toString().endsWith("{")) s.append(p);
            else s.append(",").append(p);
        }
        s.append("},parents:{");
        for(String p : parents){
            if(s.toString().endsWith("{")) s.append(p);
            else s.append(",").append(p);
        }
        s.append("},weight:").append(weight);
        return s.toString();
    }
}

