package dev.elrol.serverutilities.init;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.VoteConfig;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import dev.elrol.serverutilities.libs.ModInfo;
import dev.elrol.serverutilities.libs.text.TextUtils;
import com.vexsoftware.votifier.VoteHandler;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.net.VotifierServerBootstrap;
import com.vexsoftware.votifier.net.VotifierSession;
import com.vexsoftware.votifier.net.protocol.v1crypto.RSAIO;
import com.vexsoftware.votifier.net.protocol.v1crypto.RSAKeygen;
import com.vexsoftware.votifier.platform.LoggingAdapter;
import com.vexsoftware.votifier.platform.VotifierPlugin;
import com.vexsoftware.votifier.platform.scheduler.ScheduledVotifierTask;
import com.vexsoftware.votifier.platform.scheduler.VotifierScheduler;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.security.Key;
import java.security.KeyPair;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Votifier implements VoteHandler, VotifierPlugin {
    private static final ScheduledThreadPoolExecutor EXECUTOR = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    private final VoteConfig config = new VoteConfig();
    private KeyPair keyPair;
    private VotifierServerBootstrap bootstrap;

    public boolean bind() {
        config.load();
        //Creating the RSA directory and keys
        File dir = new File(ModInfo.Constants.configdir, "/vote/rsa/");
        try {
            if(!dir.exists()) {
                if(!dir.mkdirs()) {
                    throw new RuntimeException("Can't make RSA folder");
                }
                keyPair = RSAKeygen.generate(2048);
                RSAIO.save(dir,keyPair);
            } else {
                keyPair = RSAIO.load(dir);
            }
        } catch (Exception e) {
            Logger.err("Error creating or reading RSA tokens");
            e.printStackTrace();
            return false;
        }
        if(config.port >= 0) {
            if (config.disableV1Protocol) {
                Logger.log("------------------------------------------------------------------------------");
                Logger.log("Votifier protocol v1 parsing has been disabled. Most voting websites do not");
                Logger.log("currently support the modern Votifier protocol.");
                Logger.log("------------------------------------------------------------------------------");
            }

            bootstrap = new VotifierServerBootstrap(config.host, config.port, this, config.disableV1Protocol);
            bootstrap.start(err -> {});
        } else {
            Logger.log("------------------------------------------------------------------------------");
            Logger.log("Your Votifier port is less than 0, so we assume you do NOT want to start the");
            Logger.log("votifier port server! Votifier will not listen for votes over any port, and");
            Logger.log("will only listen for pluginMessaging forwarded votes!");
            Logger.log("------------------------------------------------------------------------------");
        }
        return true;
    }

    public Map<String, String> getLinks() {
        return config.voteLinks;
    }

    public void halt() {
        if(bootstrap != null) {
            EXECUTOR.shutdown();
            bootstrap.shutdown();
            bootstrap = null;
        }
    }

    public boolean reload() {
        try {
            halt();
        } catch (Exception e) {
            Logger.err("Halting failed, this could be fine.");
        }

        if(bind()) {
            Logger.log("Reloading Votes was successful");
            return true;
        } else {
            try {
                halt();
                Logger.err("When reloading an error was found in the configs.");
            } catch (Exception e) {
                Logger.err("When reloading an error was found and votes couldn't be stopped.");
            }
            return false;
        }
    }

    @Override
    public void onVoteReceived(Vote vote, VotifierSession.ProtocolVersion protocolVersion, String remoteAddress) throws Exception {
        String text = "&2" + vote.getUsername() + " &ajust voted on &2" + vote.getServiceName() + "&a!";
        Main.textUtils.sendToChat(text);
        ServerPlayer player = Methods.getPlayerFromName(vote.getUsername());
        if(player == null) {
            UUID uuid = Methods.getUUIDFromProfileName(vote.getUsername());
            if(uuid == null) return;
            IPlayerData playerData = Main.database.get(uuid);
            playerData.addVoteReward();
        } else {
            grantVoteReward(player);
        }
    }

    public void grantVoteReward(ServerPlayer player) {
        Random rand = new Random();
        float chance = rand.nextFloat(getTotalWeight());
        for(VoteConfig.VoteReward reward : config.rewards) {
            chance -= reward.weight;
            if(chance <= 0) {
                reward.redeem(player);
                return;
            }
        }
    }

    private float getTotalWeight() {
        float weight = 0.0f;
        for(VoteConfig.VoteReward reward : config.rewards) {
            weight+=reward.weight;
        }
        return weight;
    }

    @Override
    public Map<String, Key> getTokens() {
        return config.tokenMap;
    }

    @Override
    public KeyPair getProtocolV1Key() {
        return keyPair;
    }

    @Override
    public LoggingAdapter getPluginLogger() {
        return new VoteLogger();
    }

    @Override
    public VotifierScheduler getScheduler() {
        return new VoteScheduler();
    }

    private static class VoteLogger implements LoggingAdapter {

        @Override
        public void error(String s) {
            Logger.err(s);
        }

        @Override
        public void error(String s, Object... o) {
            Logger.err(s);
            for (Object o1 : o) {
                Logger.err(o1.toString());
            }
        }

        @Override
        public void error(String s, Throwable e, Object... o) {
            Logger.err(s);
            Logger.err(e.toString());
            for (Object o1 : o) {
                Logger.err(o1.toString());
            }
        }

        @Override
        public void warn(String s) {
            Logger.warn(s);
        }

        @Override
        public void warn(String s, Object... o) {
            Logger.warn(s);
            for (Object o1 : o) {
                Logger.warn(o1.toString());
            }
        }

        @Override
        public void info(String s) {
            Logger.log(s);
        }

        @Override
        public void info(String s, Object... o) {
            Logger.log(s);
            for (Object o1 : o) {
                Logger.log(o1.toString());
            }
        }
    }

    private static class VoteScheduler implements VotifierScheduler {

        @Override
        public ScheduledVotifierTask sync(Runnable runnable) {
            return delayedSync(runnable, 0, TimeUnit.MINUTES);
        }

        @Override
        public ScheduledVotifierTask onPool(Runnable runnable) {
            return sync(runnable);
        }

        @Override
        public ScheduledVotifierTask delayedSync(Runnable runnable, int delay, TimeUnit unit) {
            return new FutureWrapper(EXECUTOR.schedule(runnable, delay, unit));
        }

        @Override
        public ScheduledVotifierTask delayedOnPool(Runnable runnable, int delay, TimeUnit unit) {
            return delayedSync(runnable, delay, unit);
        }

        @Override
        public ScheduledVotifierTask repeatOnPool(Runnable runnable, int delay, int repeat, TimeUnit unit) {
            return new FutureWrapper(EXECUTOR.scheduleAtFixedRate(runnable, delay, repeat, unit));
        }

        public static void shutdown() {
            EXECUTOR.shutdown();
        }

        private static class FutureWrapper implements ScheduledVotifierTask {
            private final ScheduledFuture<?> sf;

            private FutureWrapper(ScheduledFuture<?> sf) {
                this.sf = sf;
            }

            @Override
            public void cancel() {
                sf.cancel(true);
            }
        }
    }
}
