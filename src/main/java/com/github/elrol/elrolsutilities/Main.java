package com.github.elrol.elrolsutilities;

import com.github.elrol.elrolsutilities.api.IElrolAPI;
import com.github.elrol.elrolsutilities.api.econ.IShopRegistry;
import com.github.elrol.elrolsutilities.config.Configs;
import com.github.elrol.elrolsutilities.data.*;
import com.github.elrol.elrolsutilities.discord.DiscordBot;
import com.github.elrol.elrolsutilities.events.*;
import com.github.elrol.elrolsutilities.init.BlackLists;
import com.github.elrol.elrolsutilities.init.PermRegistry;
import com.github.elrol.elrolsutilities.init.ShopRegistry;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.lang3.tuple.Pair;

@Mod(value="serverutilities")
public class Main {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    public static File dir;

    public static ServerData serverData;
    public static PlayerDatabase database;
    public static PlayerHistory history;
    public static PatreonList patreonList = new PatreonList();
    public static EconData econData = new EconData();
    public static IShopRegistry shopRegistry = new ShopRegistry();
    public static PermRegistry permRegistry = new PermRegistry();
    public static BlackLists blackLists = new BlackLists();
    public static DefaultTitles defaultTitles = new DefaultTitles();
    public static DiscordBot bot = new DiscordBot();
    public static MinecraftServer mcServer;
    public static boolean isCheatMode;
    public static Map<UUID, ScheduledFuture<?>> requests = new HashMap<>();
    public static Map<UUID, CommandDelay> commandDelays = new HashMap<>();
    public static Map<UUID, Map<String, CommandCooldown>> commandCooldowns = new HashMap<>();
    public static Map<String, Kit> kitMap = new HashMap<>();

    public Main() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        IElrolAPI.setInstance(new ElrolApi());

        if (!ModInfo.Constants.configdir.exists()) {
            ModInfo.Constants.configdir.mkdir();
        }

        econData.load();
        permRegistry.load();
        blackLists.load();
        defaultTitles.load();

        getLogger().info("Loading Configs");
        Configs.reload();

        loadKits();

        MinecraftForge.EVENT_BUS.register(new ChatEventHandler());
        MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
        MinecraftForge.EVENT_BUS.register(new LivingDropHandler());

        MinecraftForge.EVENT_BUS.register(new OnPlayerJoinHandler());
        MinecraftForge.EVENT_BUS.register(new OnPlayerLeaveHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerDeathHandler());

        MinecraftForge.EVENT_BUS.register(new BlockEventHandler());
        MinecraftForge.EVENT_BUS.register(new ChunkHandler());
        MinecraftForge.EVENT_BUS.register(new NewCommandEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerLifecycleHandler());
    }


    public static void loadKits() {
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

