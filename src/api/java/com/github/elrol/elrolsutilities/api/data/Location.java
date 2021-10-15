package com.github.elrol.elrolsutilities.api.data;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Objects;

public class Location {
    public ResourceLocation world;
    private BlockPos pos;
    private float yaw;
    private float pitch;

    public Location(Location loc) {
        this.world = loc.getWorld().getRegistryName();
        this.pos = loc.getBlockPos();
        this.pitch = loc.getPitch();
        this.yaw = loc.getYaw();
    }

    public Location(RegistryKey<World> world, BlockPos pos, float yaw, float pitch) {
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

    public RegistryKey<World> getWorld(){
        return RegistryKey.create(Registry.DIMENSION_REGISTRY,  world);
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
        return "Loc[DimId:" + this.world + ",Pos:" + this.pos + ",Yaw:" + this.yaw + ",Pitch:" + this.pitch + "]";
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