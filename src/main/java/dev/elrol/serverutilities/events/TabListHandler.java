package dev.elrol.serverutilities.events;

import dev.elrol.serverutilities.Main;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TabListHandler {

    @SubscribeEvent
    public void handleTabList(PlayerEvent.TabListNameFormat event) {
        event.setDisplayName(Main.textUtils.formatUsername(event.getEntity().getUUID()));
    }
}
