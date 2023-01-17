package dev.elrol.serverutilities.events;

import dev.elrol.serverutilities.Main;
import dev.elrol.serverutilities.api.data.IPlayerData;
import dev.elrol.serverutilities.config.FeatureConfig;
import dev.elrol.serverutilities.data.*;
import dev.elrol.serverutilities.econ.averon.AveronShopManager;
import dev.elrol.serverutilities.econ.chestshop.ChestShopManager;
import dev.elrol.serverutilities.econ.chestshop.ChestShopType;
import dev.elrol.serverutilities.init.Blacklists;
import dev.elrol.serverutilities.init.Ranks;
import dev.elrol.serverutilities.init.TimerInit;
import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.Methods;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.File;

public class ServerLifecycleHandler {

    @SubscribeEvent
    public void serverStarting(ServerStartingEvent event) {
        Main.database = new PlayerDatabase();
        Main.mcServer = event.getServer();
        Main.dimModes = DimensionGamemodes.load();
        Main.patreonList.init();
        Main.blacklists = Blacklists.load();
        Main.isCheatMode = Main.mcServer.getWorldData().getAllowCommands();
        Main.dir = Methods.getLevelDir(Main.mcServer.getWorldData().getLevelName());

        if(FeatureConfig.sign_shops_enabled.get()) {
            if (Main.isDev())
                Main.shopRegistry.registerShopManager(new AveronShopManager());
            if(FeatureConfig.chest_shops_enabled.get()) {
                if(FeatureConfig.chest_admin_buy_shops_enabled.get())
                    Main.shopRegistry.registerShopManager(new ChestShopManager(ChestShopType.AdminBuy));
                if(FeatureConfig.chest_admin_sell_shops_enabled.get())
                    Main.shopRegistry.registerShopManager(new ChestShopManager(ChestShopType.AdminSell));
                if(FeatureConfig.chest_buy_shops_enabled.get())
                    Main.shopRegistry.registerShopManager(new ChestShopManager(ChestShopType.Buy));
                if(FeatureConfig.chest_sell_shops_enabled.get())
                    Main.shopRegistry.registerShopManager(new ChestShopManager(ChestShopType.Sell));
            }
        }

        Main.serverData = JsonMethod.load(new File(Main.dir, "/data"), "serverdata.dat", ServerData.class);

        if (Main.serverData == null) {
            Main.serverData = new ServerData();
        }
        JsonMethod.save(new File(Main.dir, "/data"), "serverdata.dat", Main.serverData);
        Ranks.init();
        Main.permRegistry.save();
        Logger.log("Starting Timer");
        TimerInit.init();
        Main.bot.init();
        Main.bot.sendInfoMessage("Server is starting");
        if(FeatureConfig.votingEnabled.get()) {
            if(!Main.vote.bind())
                Logger.err("Voting was enabled, but failed to start.");
        }
    }

    @SubscribeEvent
    public void serverStopping(ServerStoppingEvent event) {
        Logger.log("Server Stopping");
        Main.bot.sendInfoMessage("Server is stopping");
        for(ServerPlayer player : Main.mcServer.getPlayerList().getPlayers()){
            IPlayerData data = Main.database.get(player.getUUID());
            data.setFly(player.getAbilities().mayfly);
            data.setFlying(player.getAbilities().flying);
            data.setGodmode(player.getAbilities().invulnerable);
            if(!data.canRankUp() && data.timeTillNextRank() != 0){
                long t = Main.mcServer.getNextTickTime() - data.timeLastOnline();
                long time = data.timeTillNextRank();
                if(time - t > 0){
                    data.setTimeTillNextRank(time - t);
                } else {
                    data.setTimeTillNextRank(0);
                    data.allowRankUp(true);
                }
            }
        }
        Logger.log("Saving PlayerData");
        Main.database.saveAll();
        Logger.log("Saving ServerData");
        JsonMethod.save(new File(Main.dir, "/data"), "serverdata.dat", Main.serverData);
        Logger.log("Saving ShopData");
        Main.shopRegistry.save();
        Logger.log("Stopping Timer");
        TimerInit.shutdown();
        CommandDelay.shutdown();
        TpRequest.shutdown();
        Main.bot.shutdown();
        Main.vote.halt();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void commandRegister(RegisterCommandsEvent event){
        Logger.log("Registering Commands");
        Main.commandRegistry.registerCommands(event.getDispatcher());
    }

}
