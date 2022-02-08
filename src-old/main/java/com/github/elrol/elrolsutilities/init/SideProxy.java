package com.github.elrol.elrolsutilities.init;

import com.github.elrol.elrolsutilities.ElrolApi;
import com.github.elrol.elrolsutilities.Main;
import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.config.Configs;
import com.github.elrol.elrolsutilities.data.EconData;
import com.github.elrol.elrolsutilities.data.PatreonList;
import com.github.elrol.elrolsutilities.events.*;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;

public class SideProxy {
    SideProxy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::commonSetupEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::proccessIMC);
    }

    private static void commonSetupEvent(FMLCommonSetupEvent event) {
    }

    private static void enqueueIMC(InterModEnqueueEvent event) {
    }

    private static void proccessIMC(InterModProcessEvent event) {
    }

    public static class Server extends SideProxy {
        public Server() {
            IElrolAPI.setInstance(new ElrolApi());

            if (!ModInfo.Constants.configdir.exists()) {
                ModInfo.Constants.configdir.mkdir();
            }
            Main.commandDelays = new HashMap<>();
            Main.commandCooldowns = new HashMap<>();
            Main.requests = new HashMap<>();
            Main.kitMap = new HashMap<>();
            Main.patreonList = new PatreonList();
            Main.econData = new EconData();

            Main.econData.load();
            Main.permRegistry.load();
            Main.blackLists.load();

            Main.getLogger().info("Loading Configs");
            Configs.reload();

            Main.loadKits();

            MinecraftForge.EVENT_BUS.register(new ChatEventHandler());
            MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
            MinecraftForge.EVENT_BUS.register(new LivingDropHandler());
            MinecraftForge.EVENT_BUS.register(new OnPlayerJoinHandler());
            MinecraftForge.EVENT_BUS.register(new OnPlayerLeaveHandler());
            MinecraftForge.EVENT_BUS.register(new PlayerDeathHandler());
            MinecraftForge.EVENT_BUS.register(new NewCommandEventHandler());
            MinecraftForge.EVENT_BUS.register(new BlockEventHandler());
            MinecraftForge.EVENT_BUS.register(new ChunkHandler());
            MinecraftForge.EVENT_BUS.register(new ServerLifecycleHandler());
        }
    }

    public static class Client extends SideProxy {
        public Client() {}
    }

}

