package com.github.elrol.elrolsutilities.init;

import java.io.File;

import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.data.PlayerData;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class SideProxy {
    SideProxy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::commonSetupEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::proccessIMC);
        MinecraftForge.EVENT_BUS.addListener(SideProxy::serverStopping);
    }

    private static void commonSetupEvent(FMLCommonSetupEvent event) {
    }

    private static void enqueueIMC(InterModEnqueueEvent event) {
    }

    private static void proccessIMC(InterModProcessEvent event) {
    }

    private static void serverStopping(FMLServerStoppingEvent event) {
        Logger.debug("Server Stopping. Saving data");
        Main.database.saveAll();
        for(ServerPlayerEntity player : Main.mcServer.getPlayerList().getPlayers()){
            PlayerData data = Main.database.get(player.getUUID());
            data.enableFly = player.abilities.mayfly;
            data.isFlying = player.abilities.flying;
            data.godmode = player.abilities.invulnerable;
            if(!data.canRankUp && data.nextRank != 0){
                long t = Main.mcServer.getNextTickTime() - data.lastOnline;
                if(data.nextRank - t > 0){
                    data.nextRank -= t;
                } else {
                    data.nextRank = 0;
                    data.canRankUp = true;
                }
            }
            Main.database.save(player.getUUID());
        }
        JsonMethod.save(new File(Main.dir, "/data"), "serverdata.dat", Main.serverData);
        Main.shopRegistry.save();
    }

    public static class Server
    extends SideProxy {
        public Server() {
            Logger.debug("Loading Server Proxy");
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Server::serverSetup);
        }

        private static void serverSetup(FMLDedicatedServerSetupEvent event) {
        }
    }

    public static class Client
    extends SideProxy {
        public Client() {
            Logger.debug("Loading Client Proxy");
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::clientSetup);
        }

        private static void clientSetup(FMLClientSetupEvent event) {
        }
    }

}

