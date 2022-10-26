package dev.elrol.serverutilities.data;

import com.mojang.authlib.GameProfile;
import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.claims.IClaimSetting;
import dev.elrol.serverutilities.api.data.*;
import dev.elrol.serverutilities.api.enums.ClaimFlagKeys;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.ModInfo;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

import java.time.LocalDateTime;
import java.util.*;


public class PlayerData implements IPlayerData {

    public final UUID uuid;
    public LocalDateTime firstJoin;
    public int minPlayed;
    public UUID lastMsg;
    public String username;
    public String nickname;
    public String title;
    public Map<String, Location> homes;
    public Map<String, Integer> kitCooldowns;
    public Map<ClaimFlagKeys, Boolean> claimFlags;
    public List<IClaimSetting> claimWideFlags;
    public Map<ChunkPos, IClaimSetting> chunkClaimFlags;
    private List<String> perms;
    private List<String> ranks;
    private final List<UUID> trusted;
    public List<Location> shops;
    public Location prevLoc;
    public ITpRequest tpRequest;
    public long lastOnline;
    public long nextRank;
    private long discordID;
    public int maxHomes;
    public int maxClaims;
    public int maxLoadedClaims;
    public int maxShops;
    public boolean canRankUp;
    public boolean hasBeenRtpWarned;
    public boolean enableFly;
    public boolean isFlying;
    public boolean godmode;
    public boolean disableMsg;
    public boolean firstKit;
    private double balance = 0;
    private int voteRewardCount = 0;

    private long jailed = 0;
    public String jail = "";
    public int cell = -1;

    public transient boolean bypass;
    public transient boolean staffChatEnabled;
    public transient Location signLocation;
    public transient boolean isPatreon = false;
    private transient int tempMaxClaims;
    private transient int tempMaxLoadedClaims;
    private transient String discordVerification;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.perms = new ArrayList<>();
        this.ranks = new ArrayList<>();
        this.trusted = new ArrayList<>();
        this.homes = new HashMap<>();
        this.kitCooldowns = new HashMap<>();
        this.claimFlags =  new HashMap<>();
        this.shops = new ArrayList<>();
        this.ranks.add(Ranks.default_rank.getName());
        ServerPlayer player = Main.mcServer.getPlayerList().getPlayer(uuid);
        if(player != null) this.username = player.getName().getString();
        else username = "";
        if(Main.isCheatMode) {
            Logger.log("Cheat mode is enabled ");
            ranks.add(Ranks.op_rank.getName());
        }
        this.nickname = "";
    }

    public UUID getUUID() {
        return uuid;
    }

    public void update() {
        if (perms == null) perms = new ArrayList<>();
        if (ranks == null) ranks = new ArrayList<>();
        if (nickname == null) nickname = "";
        if (kitCooldowns == null) kitCooldowns = new HashMap<>();
        if (username == null) username = "";
        if (shops == null) shops = new ArrayList<>();
        if (jail == null) jail = "";

        if(claimFlags != null) {
            for(Map.Entry<ClaimFlagKeys, Boolean> entry : claimFlags.entrySet()) {
                switch (entry.getKey()) {
                    //case allow_entry: claimWideFlags.add(new CFAllowEntry(entry.getValue())); break;
                    //case allow_pickup: claimWideFlags.add(new CFAllowPickup(entry.getValue())); break;
                }

            }
        }

        if (Main.commandDelays.containsKey(uuid)) {
            CommandDelay delay = Main.commandDelays.get(uuid);
            if (delay.seconds <= 0) Main.commandDelays.remove(uuid);
        }
        ServerPlayer player = Main.mcServer.getPlayerList().getPlayer(uuid);
        if(player != null) {
            long t = Main.mcServer.getNextTickTime() - lastOnline;
            long min = t / 60000;
            minPlayed += min;
            lastOnline = Main.mcServer.getNextTickTime();
            if(isJailed() && !player.gameMode.getGameModeForPlayer().equals(GameType.ADVENTURE)){
                player.setGameMode(GameType.ADVENTURE);
            }
            if(getDomRank() != null && !getDomRank().getNextRanks().isEmpty() && !canRankUp) {
                if(nextRank == 0){
                    nextRank = getDomRank().getRankUp();
                }
                Main.getLogger().info("Rankup in " + nextRank + " ticks");
                if(nextRank - t > 0){
                    nextRank -= t;
                } else {
                    nextRank = 0;
                    canRankUp = true;
                    Main.textUtils.msg(player, ((MutableComponent) Msgs.rankup.get()).withStyle(ChatFormatting.GREEN));
                }
            }
            save();
        }
        if(isStaff() && !Main.serverData.staffList.contains(uuid)) {
            Main.serverData.staffList.add(uuid);
        } else if(!isStaff()) {
            Main.serverData.staffList.remove(uuid);
        }
        checkPerms();
    }

    public int getMinPlayed() {
        return minPlayed;
    }

    public String getTimePlayed() {
        return Main.textUtils.ticksToTime(minPlayed);
    }

    public void jail(String jail, int cell, int min) {
        this.jail = jail;
        this.cell = cell;
        jailed = min * 60L;
        update();
    }

    public void unjail() {
        this.jail = "";
        this.cell = -1;
        jailed = 0;
        update();
    }

    public long getJailTime() { return Math.round((float)jailed / 60F); }

    public boolean isJailed() {
        return jailed > 0;
    }

    public String getJail() { return jail; }

    public int getJailCell() { return cell; }

    public boolean isStaff() {
        return hasPerm(Main.permRegistry.getPerm("staffchat")) || hasPerm("*");
    }

    public void toggleBypass() {
        bypass = !bypass;
    }

    public boolean canBypass() { return bypass; }

    public boolean charge(double cost){
        Main.getLogger().info("Charging " + this.getDisplayName() + " " + Main.textUtils.parseCurrency(cost, false));
        if(cost > balance) return false;
        setBal(balance - cost);
        Main.getLogger().info("Charged " + this.getDisplayName() + " " + Main.textUtils.parseCurrency(cost, false));
        return true;
    }

    public void pay(double amount){
        Main.getLogger().info("Paying " + this.getDisplayName() + " " + Main.textUtils.parseCurrency(amount, false));
        setBal(balance + amount);
        Main.getLogger().info("Paid " + this.getDisplayName() + " " + Main.textUtils.parseCurrency(amount, false));
        Main.getLogger().info("Balance " + balance);
    }

    public double getBal(){ return balance; }

    public void setBal(double newBal){
        balance = newBal;
        save();
    }

    public List<String> getPerms() {
        ArrayList<String> permList = new ArrayList<>(perms);
        if (this.ranks.isEmpty()) {
            return permList;
        }
        for (IRank rank : getRankList()) {
            if (rank == null) {
                Logger.err("Rank was null!");
                continue;
            }
            if (rank.getPerms().isEmpty()){
                Logger.debug(rank.getName() + " has no permissions.");
                continue;
            }
            rank.getPerms().forEach(perm -> {if(!permList.contains(perm)) permList.add(perm);});
        }
        return permList;
    }

    public Set<String> getHomeNames() {
        return this.homes.keySet();
    }

    public String getHome(String name) {
        for (String home : this.getHomeNames()) {
            if (!home.equalsIgnoreCase(name)) continue;
            return home;
        }
        return name;
    }

    public void addPerm(String p){
        if(perms.contains(p)) return;
        perms.add(p);
        checkPerm(p);
        Main.mcServer.getPlayerList().sendPlayerPermissionLevel(Methods.getPlayerFromUUID(uuid));
        save();
    }

    public void removePerm(String p){
        if(!perms.contains(p)) return;
        perms.remove(p);
        if(p.startsWith(CommandConfig.home_max_perm.get())) maxHomes = 0;
        if(p.startsWith(CommandConfig.claim_max_perm.get())) maxClaims = 0;
        if(p.startsWith(CommandConfig.claim_max_load.get())) maxLoadedClaims = 0;
        checkPerms();
        Main.mcServer.getPlayerList().sendPlayerPermissionLevel(Methods.getPlayerFromUUID(uuid));
    }

    public void clearPerms(){
        perms.clear();
        checkPerms();
        save();
        Main.mcServer.getPlayerList().sendPlayerPermissionLevel(Methods.getPlayerFromUUID(uuid));
    }

    public void checkPerms() {
        Main.getLogger().debug("Checking Perms");
        getPerms().forEach(this::checkPerm);
        if(ModList.get().isLoaded(ModInfo.Constants.ftbchunkId)) {
            Optional<GameProfile> p = Main.mcServer.getProfileCache().get(uuid);
            p.ifPresent(profile -> {
                if (maxClaims < tempMaxClaims) {
                    Main.mcServer.getCommands().performPrefixedCommand(Main.mcServer.createCommandSourceStack(), "ftbchunks admin extra_claim_chunks " + profile.getName() + " set " + tempMaxClaims);
                    maxClaims = tempMaxClaims;
                    tempMaxClaims = 0;
                }
                if (maxLoadedClaims < tempMaxLoadedClaims) {
                    Main.mcServer.getCommands().performPrefixedCommand(Main.mcServer.createCommandSourceStack(), "ftbchunks admin extra_force_load_chunks " + profile.getName() + " set " + tempMaxLoadedClaims);
                    maxLoadedClaims = tempMaxLoadedClaims;
                }
            });

        }
        if(maxClaims < tempMaxClaims) {
            maxClaims = tempMaxClaims;
        }
        save();
    }

    private void checkPerm(String p) {
        if(p == null) return;
        if(p.startsWith(CommandConfig.home_max_perm.get())) {
            String s = p.substring(CommandConfig.home_max_perm.get().length());
            try {
                int i = Integer.parseInt(s);
                if (maxHomes < i) maxHomes = i;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if(p.startsWith(CommandConfig.claim_max_perm.get())) {
            String s = p.substring(CommandConfig.claim_max_perm.get().length());
            try {
                int i = Integer.parseInt(s);
                if (tempMaxClaims < i) tempMaxClaims = i;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if(p.startsWith(CommandConfig.claim_max_load.get())) {
            String s = p.substring(CommandConfig.claim_max_load.get().length());
            try {
                int i = Integer.parseInt(s);
                if(tempMaxLoadedClaims < i) tempMaxLoadedClaims = i;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if(p.startsWith(CommandConfig.chestshop_max_perm.get())) {
            String s = p.substring(CommandConfig.chestshop_max_perm.get().length());
            try {
                int i = Integer.parseInt(s);
                if(maxShops < i) maxShops = i;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getRanks(){ return ranks; }

    public String getStringRanks() {
        StringBuilder r = new StringBuilder();
        for (String rank : this.ranks) {
            if (r.toString().isEmpty()) {
                r = new StringBuilder(rank);
                continue;
            }
            r.append(", ").append(rank);
        }
        return r.toString();
    }

    public boolean isTrusted(UUID uuid){
        if(Main.database.get(uuid).canBypass()) return true;
        return trusted.contains(uuid);
    }

    public void trust(UUID uuid){
        if(!trusted.contains(uuid)){
            trusted.add(uuid);
            save();
        }
    }

    public void untrust(UUID uuid){
        if(trusted.contains(uuid)){
            trusted.remove(uuid);
            save();
        }
    }

    public void addHome(String name, Location loc) {
        if ((name = getHome(name)) == null) {
            return;
        }
        if (homes.containsKey(name)) {
            homes.replace(name, loc);
        } else {
            homes.put(name, loc);
        }
        save();
    }

    public void delHome(String name) {
        name = getHome(name);
        homes.remove(name);
        save();
    }

    public void tickJail() {
        ServerPlayer player = Methods.getPlayerFromUUID(uuid);
        if(player == null) return;
        if(jail != null && jail.isEmpty()) return;
        if(cell < 0) return;
        if(isJailed()) {
            Location loc = Main.serverData.getJailCell(jail, cell);
            int max = FeatureConfig.jailMaxDistance.get();
            double dist = player.position().distanceTo(new Vec3(loc.x(), loc.y(), loc.z()));
            if(max > 0 && dist >= max) {
                Methods.teleport(player, loc, false);
            }
            jailed -= 1;
        } else {
            jailed = 0;
            jail = "";
            cell = -1;
            player.setGameMode(GameType.SURVIVAL);
            Methods.teleport(player, prevLoc, false);
            Main.textUtils.msg(player, Msgs.unjailed.get());
        }
        save();
    }

    public boolean hasPermOrOp(String perm) {
        return getPerms().contains("*") || hasPerm(perm);
    }

    public boolean hasPerm(String perm) {
        List<String> perms = getPerms();
        for(String p : perms) {
            if(p == null) continue;
            p = p.toLowerCase();
            if(p.contains(".*")) {
                if(perm.startsWith(p.replace(".*", ""))) {
                    return true;
                }
            } else {
                if(p.startsWith(perm.toLowerCase())) {
                    return true;
                }
            }
        }
        Logger.debug(username + " does not have the [" + perm + "] permission");
        return false;
    }

    public List<IRank> getRankList() {
        ArrayList<IRank> rankList = new ArrayList<>();
        Map<String, Rank> map = Ranks.rankMap;
        for (String name : this.ranks) {
            if (!map.containsKey(name)) continue;
            rankList.add(map.get(name));
        }
        return rankList;
    }

    public void addRank(IRank rank){
        if(ranks.contains(rank.getName())){
            Main.getLogger().error(this.username + " already has the " + rank.getName());
            return;
        }
        IRank domRank = getDomRank();
        if(domRank != null && domRank.equals(rank)) {
            if (rank.getRankUp() > 0 && !rank.getNextRanks().isEmpty()) {
                lastOnline = Main.mcServer.getNextTickTime();
                nextRank = rank.getRankUp();
                update();
            }
        }
        ranks.add(rank.getName());
        checkPerms();
        if(!rank.getCmds().isEmpty()) rank.runCmds(uuid);
        Main.mcServer.getPlayerList().sendPlayerPermissionLevel(Methods.getPlayerFromUUID(uuid));
        Main.bot.updateRoles(uuid);
    }

    public void setRank(IRank rank){
        ranks.clear();
        addRank(rank);
    }

    public void removeRank(String rank){
        if(ranks.contains(rank)) {
            ranks.remove(rank);
            checkPerms();
            Main.mcServer.getPlayerList().sendPlayerPermissionLevel(Methods.getPlayerFromUUID(uuid));
        }
    }

    public IRank getDomRank() {
        List<IRank> rankList = getRankList();
        IRank rank = null;
        for (IRank r : rankList) {
            if (rank == null) {
                rank = r;
                continue;
            }
            if (rank.getWeight() >= r.getWeight()) continue;
            rank = r;
        }
        return rank;
    }

    public void setFlag(ClaimFlagKeys key, Boolean flag){
        claimFlags.put(key, flag);
        save();
    }

    public boolean getFlag(ClaimFlagKeys key){
        return claimFlags.get(key);
    }

    public String getPrefix(){
        IRank rank = getDomRank();
        String prefix = "";
        if(rank != null) prefix = rank.getPrefix();
        return prefix;
    }

    public String getSuffix(){
        IRank rank = getDomRank();
        String suffix = "";
        if(rank != null) suffix = rank.getSuffix();
        return suffix;
    }

    public ServerPlayer getLastMsg() {
        if (this.lastMsg != null && Methods.getPlayerFromUUID(this.lastMsg) != null) {
            return Methods.getPlayerFromUUID(this.lastMsg);
        }
        this.lastMsg = null;
        return null;
    }

    public void setLastMsg(UUID uuid) {
        lastMsg = uuid;
    }

    public Long tillUseKit(IKit kit) {
        if (kitCooldowns.containsKey(kit.name)) {
            if(kit.cooldown < 1) {
                return -1L;
            }
            int used = this.kitCooldowns.get(kit.name);
            Logger.debug("" + used);
            long now = Methods.tickToMin(Main.mcServer.getNextTickTime());
            Logger.debug("" + now);
            Logger.debug("" + (used + (long)kit.cooldown) + "?" + now);
            if (used + (long)kit.cooldown <= now) {
                this.kitCooldowns.remove(kit.name);
                return 0L;
            }
            return used + (long) kit.cooldown - now;
        }
        if(kit.cooldown < 1) {
            kitCooldowns.put(kit.name, -1);
            save();
        }
        return 0L;
    }

    public String getDisplayName() {
        if (this.nickname.isEmpty()) {
            if (this.username.isEmpty()) {
                return this.uuid.toString();
            }
            return this.username;
        }
        return Main.textUtils.formatString(this.nickname) + ChatFormatting.RESET;
    }

    public void addShop(Location loc) {
        if(shops.contains(loc)) return;
        shops.add(loc);
    }
    public void removeShop(Location loc) { shops.remove(loc); }

    public long getDiscordID() { return discordID; }
    public void setDiscordID(long id) {
        discordID = id;
        Main.bot.updateRoles(uuid);
        IElrolAPI.getInstance().getPlayerDatabase().link(uuid, id);
    }

    public String getTitle(){ return title == null ? "" : title; }
    public void setTitle(String title) { this.title = title; }

    public void setFly(boolean flag)                            { this.enableFly = flag; }
    public boolean canFly()                                     { return enableFly; }
    public void setFlying(boolean flag)                         { this.isFlying = flag; }
    public boolean isFlying()                                   { return isFlying; }
    public void setGodmode(boolean flag)                        { this.godmode = flag; }
    public boolean hasGodmode()                                 { return godmode; }
    public boolean canRankUp()                                  { return canRankUp; }
    public void allowRankUp(boolean flag)                       { canRankUp = flag; }
    public long timeTillNextRank()                              { return nextRank; }
    public void setTimeTillNextRank(long time)                  { nextRank = time; }
    public long timeLastOnline()                                { return lastOnline; }
    public List<Location> getShops()                            { return shops; }
    public Location getPrevLoc()                                { return prevLoc; }
    public boolean msgDisabled()                                { return disableMsg; }
    public int getMaxClaims()                                   { return maxClaims; }
    public int getMaxHomes()                                    { return maxHomes; }
    public int getMaxShops()                                    { return maxShops; }
    public Map<ClaimFlagKeys, Boolean> getClaimFlags()          { return claimFlags; }
    public Map<String, Location> getHomes()                     { return homes; }
    public ITpRequest getTpRequest()                            { return tpRequest; }
    public void setTpRequest(ITpRequest tpRequest)              { this.tpRequest = tpRequest; }
    public Map<String, Integer> getKitCooldowns()               { return kitCooldowns; }
    public void setMsgDisabled(boolean flag)                    { this.disableMsg = flag; }
    public void setNickname(String name)                        { this.nickname = name; }
    public String getNickname()                                 { return nickname; }
    public boolean hasBeenWarned()                              { return this.hasBeenRtpWarned; }
    public void setHasBeenWarned(boolean flag)                  { this.hasBeenRtpWarned = flag; }
    public void toggleStaffChat()                               { staffChatEnabled = !staffChatEnabled; }
    public boolean usingStaffChat()                             { return staffChatEnabled;}
    public LocalDateTime getFirstJoin()                         { return firstJoin; }
    public void setUsername(String name)                        { username = name; }
    public String getUsername()                                 { return username; }
    public boolean isPatreon()                                  { return isPatreon; }
    public void setPatreon(boolean flag)                        { isPatreon = flag; }
    public boolean gotFirstKit()                                { return firstKit; }
    public void gotFirstKit(boolean flag)                       { firstKit = flag; }
    public void setLastOnline(long time)                        { lastOnline = time; }
    public void setPrevLoc(Location loc)                        { prevLoc = loc; }
    public int getVoteRewardCount()                             { return voteRewardCount; }
    public void addVoteReward()                                 { voteRewardCount++; save(); }
    public void clearVoteReward()                               { voteRewardCount = 0; save(); }

    public void save()                                          { Main.database.save(this); }
}

