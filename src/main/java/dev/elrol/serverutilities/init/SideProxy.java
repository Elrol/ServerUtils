package dev.elrol.serverutilities.init;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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

