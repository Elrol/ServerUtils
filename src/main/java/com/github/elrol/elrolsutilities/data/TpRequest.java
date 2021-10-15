package com.github.elrol.elrolsutilities.data;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;

import net.minecraft.entity.player.ServerPlayerEntity;

public class TpRequest implements Runnable, Serializable {
    private static final long serialVersionUID = 4294708795194730636L;
    private UUID requester;
    private UUID target;
    private boolean tpHere;

    public TpRequest(ServerPlayerEntity requester, ServerPlayerEntity target, boolean tpHere) {
        this.requester = requester.getUUID();
        this.target = target.getUUID();
        this.tpHere = tpHere;
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> a = s.schedule(this, 30L, TimeUnit.SECONDS);
        if (Main.requests.containsKey(this.target)) {
            this.cancel();
        }
        Main.requests.put(this.target, a);
        PlayerData data = Main.database.get(target.getUUID());
        if (data.tpRequest != null) {
            data.tpRequest.cancel();
        }
        data.tpRequest = this;
    }

    public void accept() {
        final ServerPlayerEntity r = Methods.getPlayerFromUUID(this.requester);
        final ServerPlayerEntity t = Methods.getPlayerFromUUID(this.target);
        TextUtils.msg(t, Msgs.accepted_tp(Methods.getDisplayName(r)));
        TextUtils.msg(r, Msgs.accepted_your_tp(Methods.getDisplayName(t)));
        if (this.tpHere) {
            CommandDelay.init(t, CommandConfig.tpa_tp_time.get(), () -> Methods.teleport(t, Methods.getPlayerLocation(r)), true);
        } else {
            CommandDelay.init(r, CommandConfig.tpa_tp_time.get(), () -> Methods.teleport(r, Methods.getPlayerLocation(t)), true);
        }
        this.cancel();
    }

    public void deny() {
        ServerPlayerEntity r = Methods.getPlayerFromUUID(this.requester);
        ServerPlayerEntity t = Methods.getPlayerFromUUID(this.target);
        TextUtils.err(t, Errs.denied_tp(Methods.getDisplayName(r)));
        TextUtils.err(r, Errs.denied_your_tp(Methods.getDisplayName(t)));
        this.cancel();
    }

    @Override
    public void run() {
        ServerPlayerEntity r = Methods.getPlayerFromUUID(this.requester);
        ServerPlayerEntity t = Methods.getPlayerFromUUID(this.target);
        TextUtils.err(t, Errs.tp_expired(Methods.getDisplayName(r)));
        TextUtils.err(r, Errs.your_tp_expired(Methods.getDisplayName(t)));
        this.cancel();
    }

    public void cancel() {
        ScheduledFuture<?> a = Main.requests.get(this.target);
        a.cancel(true);
        Main.requests.remove(this.target);
        PlayerData data = Main.database.get(this.target);
        data.tpRequest = null;
    }

}

