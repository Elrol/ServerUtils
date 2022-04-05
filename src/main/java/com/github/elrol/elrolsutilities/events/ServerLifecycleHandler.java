package com.github.elrol.elrolsutilities.events;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.data.IPlayerData;
import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.data.*;
import com.github.elrol.elrolsutilities.econ.averon.AveronShopManager;
import com.github.elrol.elrolsutilities.econ.chestshop.ChestShopManager;
import com.github.elrol.elrolsutilities.econ.chestshop.ChestShopType;
import com.github.elrol.elrolsutilities.init.CommandRegistry;
import com.github.elrol.elrolsutilities.init.Ranks;
import com.github.elrol.elrolsutilities.init.TimerInit;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.Methods;
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
        Main.patreonList.init();
        Main.isCheatMode = Main.mcServer.getWorldData().getAllowCommands();
        Main.dir = Methods.getLevelDir(Main.mcServer.getWorldData().getLevelName());

        Main.shopRegistry.registerShopManager(new AveronShopManager());
        Main.shopRegistry.registerShopManager(new ChestShopManager(ChestShopType.AdminBuy));
        Main.shopRegistry.registerShopManager(new ChestShopManager(ChestShopType.AdminSell));
        Main.shopRegistry.registerShopManager(new ChestShopManager(ChestShopType.Buy));
        Main.shopRegistry.registerShopManager(new ChestShopManager(ChestShopType.Sell));
        Main.serverData = JsonMethod.load(new File(Main.dir, "/data"), "serverdata.dat", ServerData.class);

        if (Main.serverData == null) {
            Main.serverData = new ServerData();
        }
        JsonMethod.save(new File(Main.dir, "/data"), "serverdata.dat", Main.serverData);
        Ranks.init();
        Main.permRegistry.save();
        Logger.log("Starting Timer");
        //TimerInit.init();
        Main.bot.init();
        Main.bot.sendInfoMessage("Server is starting");
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
        CommandCooldown.shutdown();
        CommandDelay.shutdown();
        TpRequest.shutdown();
        Main.bot.shutdown();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void commandRegister(RegisterCommandsEvent event){
        Logger.log("Registering Commands");
        CommandRegistry.registerCommands(event.getDispatcher());
    }

}
