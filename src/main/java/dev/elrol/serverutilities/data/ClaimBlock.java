package dev.elrol.serverutilities.data;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

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
