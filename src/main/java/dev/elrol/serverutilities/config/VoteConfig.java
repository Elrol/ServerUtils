package dev.elrol.serverutilities.config;

import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.ModInfo;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteConfig {

    transient File dir = new File(ModInfo.Constants.configdir, "/vote/");

    public String host = "";
    public int port = 0;
    public Map<String, Key> tokenMap = new HashMap<>();
    public Map<String, String> voteLinks = new HashMap<>();
    public List<VoteReward> rewards = new ArrayList<>();
    public boolean disableV1Protocol;

    public void save() {
        if(voteLinks.isEmpty()) voteLinks.put("Default Website Name", "https://default.webiste.address");
        if(rewards.isEmpty()){
            VoteReward reward = new VoteReward();
            reward.commands.add("give {player} dirt");
            reward.weight = 1.0f;
            rewards.add(reward);
        }
        JsonMethod.save(dir,"VoteSettings.json", this);
    }
    public void load() {
        VoteConfig config = JsonMethod.load(dir, "VoteSettings.json", getClass());
        if(config != null) {
            host = config.host;
            port = config.port;
            tokenMap = config.tokenMap;
            voteLinks = config.voteLinks;
            disableV1Protocol = config.disableV1Protocol;
            rewards = config.rewards;
        } else {
            save();
        }
    }

    public static class VoteReward {
        public final List<String> commands = new ArrayList<>();
        public float weight = 0.0f;

        public void redeem(ServerPlayer player) {
            commands.forEach(cmd-> {
                Methods.runCommand(cmd.replace("{player}", player.getName().getString()));
            });
        }
    }

}
