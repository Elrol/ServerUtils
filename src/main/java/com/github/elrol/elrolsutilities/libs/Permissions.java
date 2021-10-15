package com.github.elrol.elrolsutilities.libs;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.init.PermRegistry;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static final List<String> defaultRankPerms() {
        PermRegistry reg = Main.permRegistry;
        ArrayList<String> perms = new ArrayList<String>();
        perms.add(reg.getPerm("back"));
        perms.add(reg.getPerm("delhome"));
        perms.add(reg.getPerm("home"));
        perms.add(reg.getPerm("home.name"));
        perms.add(reg.getPerm("homes"));
        perms.add(CommandConfig.home_max_perm.get() + "1");
        perms.add(CommandConfig.claim_max_perm.get() + "9");
        perms.add(reg.getPerm("homes"));
        perms.add(reg.getPerm("msg.players.msg"));
        perms.add(reg.getPerm("msgtoggle"));
        perms.add(reg.getPerm("r.msg"));
        perms.add(reg.getPerm("rtp"));
        perms.add(reg.getPerm("rtpnear"));
        perms.add(reg.getPerm("rtpfar"));
        perms.add(reg.getPerm("sethome"));
        perms.add(reg.getPerm("sethome.name"));
        perms.add(reg.getPerm("spawn"));
        perms.add(reg.getPerm("tpa"));
        perms.add(reg.getPerm("tpahere"));
        perms.add(reg.getPerm("tpdeny"));
        perms.add(reg.getPerm("tpaccept"));
        perms.add(reg.getPerm("warp.name"));
        perms.add(reg.getPerm("warps"));

        return perms;
    }
}

