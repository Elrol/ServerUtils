package dev.elrol.serverutilities.libs;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.init.PermRegistry;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static final List<String> defaultRankPerms() {
        PermRegistry reg = Main.permRegistry;
        ArrayList<String> perms = new ArrayList<String>();
        perms.add(reg.getPerm("back"));
        perms.add(reg.getPerm("claim"));
        perms.add(reg.getPerm("delhome_name"));
        perms.add(reg.getPerm("home_name"));
        perms.add(reg.getPerm("homes"));
        perms.add(CommandConfig.home_max_perm.get() + "1");
        perms.add(CommandConfig.claim_max_perm.get() + "9");
        perms.add(reg.getPerm("kit_claim_kit"));
        perms.add(reg.getPerm("kit_verification"));
        perms.add(reg.getPerm("msg_players_msg"));
        perms.add(reg.getPerm("msgtoggle"));
        perms.add(reg.getPerm("rank_info_rank"));
        perms.add(reg.getPerm("r_msg"));
        perms.add(reg.getPerm("rtp"));
        perms.add(reg.getPerm("rtpnear"));
        perms.add(reg.getPerm("rtpfar"));
        perms.add(reg.getPerm("sethome_name"));
        perms.add(reg.getPerm("spawn"));
        perms.add(reg.getPerm("tpa_player"));
        perms.add(reg.getPerm("tpahere_player"));
        perms.add(reg.getPerm("tpdeny"));
        perms.add(reg.getPerm("tpaccept"));
        perms.add(reg.getPerm("unclaim"));
        perms.add(reg.getPerm("warp_name"));
        perms.add(reg.getPerm("warps"));

        return perms;
    }
}

