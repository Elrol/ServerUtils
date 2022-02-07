package com.github.elrol.elrolsutilities.api.data;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Objects;

public class Location {
    public ResourceLocation world;
    private BlockPos pos;
    private float yaw = 0f;
    private float pitch = 0f;

    public Location(BlockEntity entity){
        this.world = entity.getLevel().dimension().location();
        this.pos = entity.getBlockPos();
    }

    public Location(ResourceLocation dim, BlockPos pos) {
        this.world = dim;
        this.pos = pos;
    }

    public Location(Location loc) {
        this.world = loc.world;
        this.pos = loc.pos;
        this.pitch = loc.pitch;
        this.yaw = loc.yaw;
    }

    public Location(Level world, BlockPos pos, float yaw, float pitch) {
        this.world = world.dimension().location();
        this.pos = pos;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Location(ResourceKey<Level> world, BlockPos pos, float yaw, float pitch) {
        this.world = world.location();
        this.pos = pos;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Location(ResourceLocation world, BlockPos pos, float yaw, float pitch) {
        this.world = world;
        this.pos = pos;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public ResourceKey<Level> getLevel(){
        return ResourceKey.create(Registry.DIMENSION_REGISTRY,  world);
    }

    public ServerLevel getLevelObj(){
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server.getLevel(getLevel());
    }

    public BlockPos getBlockPos() {
        return this.pos;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public int x() {
        return this.getBlockPos().getX();
    }

    public int y() {
        return this.getBlockPos().getY();
    }

    public int z() {
        return this.getBlockPos().getZ();
    }

    public String toString() {
        // minecraft:overworld@[0,63,0](0.0,0.0)
        return world.toString() + "@[" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "](" + yaw + "," + pitch + ")";
    }

    public static Location fromString(String loc) {
        String[] loc1 = loc.split("@\\[");
        String[] loc2 = loc1[1].split("]\\(");
        String[] rsloc = loc1[0].split(":");

        String[] posLoc = loc2[0].split(",");
        String[] rotLoc = loc2[1].replace(")", "").split(",");

        int x = Integer.parseInt(posLoc[0]);
        int y = Integer.parseInt(posLoc[1]);
        int z = Integer.parseInt(posLoc[2]);

        return new Location(
            new ResourceLocation(rsloc[0], rsloc[1]),
            new BlockPos(x,y,z),
            Float.parseFloat(rotLoc[0]),
            Float.parseFloat(rotLoc[1])
        );
    }

    public void setYaw(float yaw) { this.yaw = yaw; }
    public void setpitch(float pitch) { this.pitch = pitch; }

    public Location modX(int x) {
        this.pos = new BlockPos(this.pos.getX() + x, this.pos.getY(), this.pos.getZ());
        return this;
    }

    public Location modY(int y) {
        this.pos = new BlockPos(this.pos.getX(), this.pos.getY() + y, this.pos.getZ());
        return this;
    }

    public Location modZ(int z) {
        this.pos = new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + z);
        return this;
    }

    public BlockEntity getBlockEntity() {
        return getLevelObj().getBlockEntity(pos);
    }

    public BlockState getBlockState() {
        return getLevelObj().getBlockState(pos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Float.compare(location.yaw, yaw) == 0 &&
                Float.compare(location.pitch, pitch) == 0 &&
                world.equals(location.world) &&
                pos.equals(location.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, pos, yaw, pitch);
    }
}