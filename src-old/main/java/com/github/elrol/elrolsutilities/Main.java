package com.github.elrol.elrolsutilities;

import com.github.elrol.elrolsutilities.api.econ.IShopRegistry;
import com.github.elrol.elrolsutilities.data.*;
import com.github.elrol.elrolsutilities.discord.DiscordBot;
import com.github.elrol.elrolsutilities.init.*;
import com.github.elrol.elrolsutilities.libs.JsonMethod;
import com.github.elrol.elrolsutilities.libs.Logger;
import com.github.elrol.elrolsutilities.libs.ModInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.io.File;
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
    public static PermRegistry permRegistry = new PermRegistry();
    public static BlackLists blackLists = new BlackLists();
    public static DiscordBot bot = new DiscordBot();
    public static MinecraftServer mcServer;
    public static boolean isCheatMode;
    public static Map<UUID, ScheduledFuture<?>> requests;
    public static Map<UUID, CommandDelay> commandDelays;
    public static Map<UUID, Map<String, CommandCooldown>> commandCooldowns;
    public static Map<String, Kit> kitMap;

    public Main() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        DistExecutor.safeRunForDist(() -> SideProxy.Client::new, () -> SideProxy.Server::new);
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

    public static void shutdown() {
        commandDelays.forEach((uuid, delay) -> delay.cancel());
        commandCooldowns.forEach((uuid, map) -> map.forEach((cmd, cd) -> cd.cancel()));
        requests.forEach((uuid, sf) -> sf.cancel(true));
        TimerInit.shutdown();
    }

    public static boolean isDev() {
        return false;
    }

    public static org.apache.logging.log4j.Logger getLogger() {
        return LOGGER;
    }
}

