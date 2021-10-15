package com.github.elrol.elrolsutilities.events;

import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ChunkHandler {

    public static List<ChunkPos> loadedChunks = new ArrayList<>();

    @SubscribeEvent
    public void onLoad(ChunkEvent.Load event){
        loadedChunks.add(event.getChunk().getPos());
    }

    @SubscribeEvent
    public void onUnload(ChunkEvent.Unload event){
        loadedChunks.remove(event.getChunk().getPos());
    }

}
