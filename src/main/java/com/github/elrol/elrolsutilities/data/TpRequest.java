package com.github.elrol.elrolsutilities.data;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.CommandConfig;
import com.github.elrol.elrolsutilities.libs.Methods;
import com.github.elrol.elrolsutilities.libs.text.Errs;
import com.github.elrol.elrolsutilities.libs.text.Msgs;
import com.github.elrol.elrolsutilities.libs.text.TextUtils;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TpRequest implements Runnable, Serializable, com.github.elrol.elrolsutilities.api.data.ITpRequest {
    private static final long serialVersionUID = 4294708795194730636L;
    private static final ScheduledThreadPoolExecutor s = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    private final UUID requester;
    private final UUID target;
    private final boolean tpHere;

    public TpRequest(ServerPlayerEntity requester, ServerPlayerEntity target, boolean tpHere) {
        this.requester = requester.getUUID();
        this.target = target.getUUID();
        this.tpHere = tpHere;
        ScheduledFuture<?> a = s.schedule(this, 30L, TimeUnit.SECONDS);
        if (Main.requests.containsKey(this.target)) {
            this.cancel();
        }
        Main.requests.put(this.target, a);
        IPlayerData data = Main.database.get(target.getUUID());
        if (data.getTpRequest() != null) {
            data.getTpRequest().cancel();
        }
        data.setTpRequest(this);
    }

    @Override
    public void accept() {
        final ServerPlayerEntity r = Methods.getPlayerFromUUID(this.requester);
        final ServerPlayerEntity t = Methods.getPlayerFromUUID(this.target);
        TextUtils.msg(t, Msgs.acceptedTp.get(Methods.getDisplayName(r)));
        TextUtils.msg(r, Msgs.acceptedYourTp.get(Methods.getDisplayName(t)));
        if (this.tpHere) {
            CommandDelay.init(t, CommandConfig.tpa_tp_time.get(), () -> Methods.teleport(t, Methods.getPlayerLocation(r)), true);
        } else {
            CommandDelay.init(r, CommandConfig.tpa_tp_time.get(), () -> Methods.teleport(r, Methods.getPlayerLocation(t)), true);
        }
        this.cancel();
    }

    @Override
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
        IPlayerData data = Main.database.get(this.target);
        data.setTpRequest(null);
    }

    public static synchronized void shutdown(){
        s.shutdown();
    }
}

