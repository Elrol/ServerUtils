package com.github.elrol.elrolsutilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.econ.IShopRegistry;
import com.github.elrol.elrolsutilities.config.Configs;
import com.github.elrol.elrolsutilities.data.*;
import com.github.elrol.elrolsutilities.events.*;
import com.github.elrol.elrolsutilities.init.*;
import com.github.elrol.elrolsutilities.libs.Methods;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;

import com.github.elrol.elrolsutilities.config.FeatureConfig;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.ModInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(value="serverutilities")
public class Main {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    public static File dir;

    public static ServerData serverData;
    public static PlayerDatabase database;
    public static PlayerHistory history;
    public static PatreonList patreonList;
    public static EconData econData;
    public static IShopRegistry shopRegistry = new ShopRegistry();
    public static PermRegistry permRegistry = new PermRegistry();
    public static BlackLists blackLists = new BlackLists();
    public static MinecraftServer mcServer;
    public static boolean isCheatMode;
    public static Map<UUID, ScheduledFuture<?>> requests;
    public static Map<UUID, CommandDelay> commandDelays;
    public static Map<UUID, Map<String, CommandCooldown>> commandCooldowns;
    public static Map<String, Kit> kitMap;

    public Main() {
        IElrolAPI.setInstance(new ElrolApi());

        if (!ModInfo.Constants.configdir.exists()) {
            ModInfo.Constants.configdir.mkdir();
        }
        commandDelays = new HashMap<>();
        commandCooldowns = new HashMap<>();
        requests = new HashMap<>();
        kitMap = new HashMap<>();
        patreonList = new PatreonList();
        econData = new EconData();
        econData.load();
        shopRegistry.load();
        permRegistry.load();
        blackLists.load();

        if(getLogger() == null) System.out.println("Logger was null?");

        getLogger().info("Loading Configs");
        Configs.reload();

        this.loadKits();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ChatEventHandler());
        MinecraftForge.EVENT_BUS.register(new EntityInteractHandler());
        MinecraftForge.EVENT_BUS.register(new LivingDropHandler());
        MinecraftForge.EVENT_BUS.register(new OnPlayerJoinHandler());
        MinecraftForge.EVENT_BUS.register(new OnPlayerLeaveHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerDeathHandler());
        MinecraftForge.EVENT_BUS.register(new NewCommandEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlockEventHandler());
        MinecraftForge.EVENT_BUS.register(new ChunkHandler());
        DistExecutor.safeRunForDist(() -> SideProxy.Client::new, () -> SideProxy.Server::new);
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event) {
        database = new PlayerDatabase();
        mcServer = event.getServer();
        patreonList.init();
        //Logger.log("Server IP is as follows: " + mcServer.getServerHostname() + ":" + mcServer.getServerPort());
        isCheatMode = mcServer.getWorldData().getAllowCommands();
        dir = Methods.getWorldDir(event.getServer().getWorldData().getLevelName());

        //shopRegistry.registerShopManager(new ChestShopManager.Buy());
        //shopRegistry.registerShopManager(new ChestShopManager.Sell());
        //shopRegistry.registerShopManager(new ChestShopManager.AdminBuy());
        //shopRegistry.registerShopManager(new ChestShopManager.AdminSell());



        serverData = JsonMethod.load(new File(dir, "/data"), "serverdata.dat", ServerData.class);
        if (serverData == null) {
            serverData = new ServerData();
        }
        JsonMethod.save(new File(dir, "/data"), "serverdata.dat", serverData);
        Ranks.init();
        database.loadAll();
        permRegistry.save();
        TimerInit.init();
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void commandRegister(RegisterCommandsEvent event){
        getLogger().info("Registering Commands");
        CommandRegistry.registerCommands(event.getDispatcher());
    }

    public void loadKits() {
        File[] kitFiles = ModInfo.Constants.kitdir.listFiles((dir, name) -> name.endsWith(".json"));
        if (ModInfo.Constants.kitdir.mkdirs()) {
            Logger.log("Kit directory was missing and has been created.");
        }
        if(kitFiles == null) return;
        for (File kitFile : kitFiles) {
            Kit kit = JsonMethod.load(ModInfo.Constants.kitdir, kitFile.getName(), Kit.class);
            if(kit == null) continue;
            if (kitMap.containsKey(kit.name)) {
                Logger.err("Kit unable to load: " + kit.name);
                continue;
            }
            kit.getPerm();
            kitMap.put(kit.name, kit);
            if (kit.name == null) continue;
            Logger.log("Kit loaded: " + kit.name);
        }
    }

    public static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById("serverutilities");
        if (o.isPresent()) {
            return (o.get()).getModInfo().getVersion().toString();
        }
        return "NONE";
    }

    public static boolean isDev() {
        return false;
    }

    public static org.apache.logging.log4j.Logger getLogger() {
        return LOGGER;
    }
}

