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

        }
    }

    public static class Client extends SideProxy {
        public Client() {}
    }

}

