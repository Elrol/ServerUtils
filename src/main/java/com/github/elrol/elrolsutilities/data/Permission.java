package com.github.elrol.elrolsutilities.data;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.perms.IPermission;

public class Permission implements IPermission {
    String perm;

    public Permission(String node, String perm) {
        this.perm = perm;
        Main.permRegistry.add(node, perm);
    }

    public Permission(String perm) {
        this.perm = perm;
        if (!perm.isEmpty() && !Main.permRegistry.getPerms().contains(perm)) {
            Main.permRegistry.add(perm);
        }
    }

    @Override
    public String get() {
        return this.perm;
    }
}

