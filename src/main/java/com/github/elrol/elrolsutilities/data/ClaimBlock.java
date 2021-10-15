package com.github.elrol.elrolsutilities.data;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class ClaimBlock {

    private ResourceLocation dim;
    private ChunkPos pos;

    public ClaimBlock(ResourceLocation dim, ChunkPos pos){
        this.dim = dim;
        this.pos = pos;
    }

    public ClaimBlock(ResourceLocation dim, int x, int z){
        this.dim = dim;
        this.pos = new ChunkPos(x, z);
    }

    public ClaimBlock(ResourceLocation dim, BlockPos pos){
        this.dim = dim;
        this.pos = new ChunkPos(pos);
    }

    public ClaimBlock(Entity entity){
        this.dim = entity.level.dimension().location();
        this.pos = new ChunkPos(new BlockPos(entity.position()));
    }

    public ResourceLocation getDim(){ return dim; }
    public ChunkPos getPos(){ return pos; }

    public String toString(){
        return dim + "," + pos.x + "," + pos.z;
    }

    public static ClaimBlock of(String string){
        if(string.contains(",")){
            String[] a = string.split(",");
            return new ClaimBlock(
                    new ResourceLocation(a[0]),
                    new ChunkPos(Integer.parseInt(a[1]), Integer.parseInt(a[2])));
        }
        return null;
    }
}
