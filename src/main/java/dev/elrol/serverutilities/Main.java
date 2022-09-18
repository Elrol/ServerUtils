package dev.elrol.serverutilities;

import dev.elrol.serverutilities.api.IElrolAPI;
import dev.elrol.serverutilities.api.econ.IShopRegistry;
import dev.elrol.serverutilities.api.init.ICommandRegistry;
import dev.elrol.serverutilities.api.init.ITextUtils;
import dev.elrol.serverutilities.config.Configs;
import dev.elrol.serverutilities.data.*;
import dev.elrol.serverutilities.discord.DiscordBot;
import dev.elrol.serverutilities.events.*;
import dev.elrol.serverutilities.init.*;
import dev.elrol.serverutilities.libs.JsonMethod;
import dev.elrol.serverutilities.libs.Logger;
import dev.elrol.serverutilities.libs.ModInfo;
import dev.elrol.serverutilities.libs.text.TextUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

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
    public static ICommandRegistry commandRegistry;
    public static ITextUtils textUtils = new TextUtils();
    public static PermRegistry permRegistry = new PermRegistry();
    public static Blacklists blacklists = Blacklists.load();
    public static DefaultTitles defaultTitles = new DefaultTitles();
    public static DiscordBot bot = DiscordBot.load();
    public static Votifier vote = new Votifier();
    public static MinecraftServer mcServer;
    public static boolean isCheatMode;
    public static Map<UUID, ScheduledFuture<?>> requests;
    public static Map<UUID, CommandDelay> commandDelays;
    public static Map<UUID, Map<String, CommandCooldown>> commandCooldowns;
    public static Map<String, Kit> kitMap;

    static {
        IElrolAPI.setInstance(new ElrolApi());
    }

    public Main() {
        new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (s,b)-> true);
        ModLoadingContext.get().registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                ()-> new IExtensionPoint.DisplayTest(
                        ()-> NetworkConstants.IGNORESERVERONLY,
                        (s, b) -> true));

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
        permRegistry.load();
        defaultTitles.load();

        if(bot == null) bot = new DiscordBot();

        getLogger().info("Loading Configs");
        Configs.reload();

        commandRegistry = new CommandRegistry();

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

