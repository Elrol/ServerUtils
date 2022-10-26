package dev.elrol.serverutilities.data;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.api.data.Location;
import dev.elrol.serverutilities.config.CommandConfig;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.text.Errs;
import dev.elrol.serverutilities.libs.text.Msgs;
import net.minecraft.server.level.ServerPlayer;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TpRequest implements Runnable, Serializable, dev.elrol.serverutilities.api.data.ITpRequest {
    @Serial
    private static final long serialVersionUID = 4294708795194730636L;
    private static final ScheduledThreadPoolExecutor s = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    private final UUID requester;
    private final UUID target;
    private final boolean tpHere;

    public TpRequest(ServerPlayer requester, ServerPlayer target, boolean tpHere) {
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
        final ServerPlayer r = Methods.getPlayerFromUUID(this.requester);
        final ServerPlayer t = Methods.getPlayerFromUUID(this.target);
        Main.textUtils.msg(t, Msgs.acceptedTp.get(Methods.getDisplayName(r)));
        Main.textUtils.msg(r, Msgs.acceptedYourTp.get(Methods.getDisplayName(t)));
        if (this.tpHere) {
            CommandDelay.init(t, CommandConfig.tpa_tp_time.get(), () -> Methods.teleport(t, new Location(r)), true);
        } else {
            CommandDelay.init(r, CommandConfig.tpa_tp_time.get(), () -> Methods.teleport(r, new Location(t)), true);
        }
        this.cancel();
    }

    @Override
    public void deny() {
        ServerPlayer r = Methods.getPlayerFromUUID(this.requester);
        ServerPlayer t = Methods.getPlayerFromUUID(this.target);
        Main.textUtils.err(t, Errs.denied_tp(Methods.getDisplayName(r)));
        Main.textUtils.err(r, Errs.denied_your_tp(Methods.getDisplayName(t)));
        this.cancel();
    }

    @Override
    public void run() {
        ServerPlayer r = Methods.getPlayerFromUUID(this.requester);
        ServerPlayer t = Methods.getPlayerFromUUID(this.target);
        Main.textUtils.err(t, Errs.tp_expired(Methods.getDisplayName(r)));
        Main.textUtils.err(r, Errs.your_tp_expired(Methods.getDisplayName(t)));
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

