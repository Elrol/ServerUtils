package dev.elrol.serverutilities.api.data;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Objects;

public class Location {
    public ResourceLocation world;
    private final Vec3 position;
    private float yaw;
    private float pitch;

    public Location(ServerPlayer player) {
        this(player.getLevel().dimension().location(), player.position(), player.getYRot(), player.getXRot());
    }

    public Location(BlockEntity entity){
        this(entity.getLevel().dimension().location(), entity.getBlockPos());
    }

    public Location(ResourceLocation dim, BlockPos pos) {
        this(dim, pos, 0.0f,0.0f);
    }

    public Location(Location loc) {
        this.world = loc.world;
        this.position = loc.position;
        this.pitch = loc.pitch;
        this.yaw = loc.yaw;
    }

    public Location(Level world, BlockPos pos, float yaw, float pitch) {
        this(world.dimension().location(), pos, yaw, pitch);
    }

    public Location(ResourceKey<Level> world, BlockPos pos, float yaw, float pitch) {
        this(world.location(), pos, yaw, pitch);
    }

    public Location(ResourceLocation world, BlockPos pos, float yaw, float pitch) {
        this(world, new Vec3(pos.getX(), pos.getY(), pos.getZ()), yaw, pitch);
    }

    public Location(ResourceLocation world, Vec3 position, float yaw, float pitch) {
        this.world = world;
        this.position = position;
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
        return new BlockPos(position);
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
        return world.toString() + "@[" + position.x() + "," + position.y() + "," + position.z() + "](" + yaw + "," + pitch + ")";
    }

    public static Location fromString(String loc) {
        String[] loc1 = loc.split("@\\[");
        String[] loc2 = loc1[1].split("]\\(");
        String[] rsloc = loc1[0].split(":");

        String[] posLoc = loc2[0].split(",");
        String[] rotLoc = loc2[1].replace(")", "").split(",");

        double x = Double.parseDouble(posLoc[0]);
        double y = Double.parseDouble(posLoc[1]);
        double z = Double.parseDouble(posLoc[2]);

        return new Location(
            new ResourceLocation(rsloc[0], rsloc[1]),
            new Vec3(x,y,z),
            Float.parseFloat(rotLoc[0]),
            Float.parseFloat(rotLoc[1])
        );
    }

    public void setYaw(float yaw) { this.yaw = yaw; }
    public void setpitch(float pitch) { this.pitch = pitch; }

    public Location modX(int x) {
        return new Location(world, position.add(x, 0, 0), yaw, pitch);
    }

    public Location modY(int y) {
        return new Location(world, position.add(0, y, 0), yaw, pitch);
    }

    public Location modZ(int z) {
        return new Location(world, position.add(0, 0, z), yaw, pitch);
    }

    public BlockEntity getBlockEntity() {
        return getLevelObj().getBlockEntity(getBlockPos());
    }

    public BlockState getBlockState() {
        return getLevelObj().getBlockState(getBlockPos());
    }

    public boolean isSamePosition(Location loc) {
        Vec3 locPos = loc.position;
        boolean flag1 = locPos.x -0.5 < position.x && locPos.x +0.5 > position.x;
        boolean flag2 = locPos.y -0.5 < position.y && locPos.y +0.5 > position.y;
        boolean flag3 = locPos.z -0.5 < position.z && locPos.z +0.5 > position.z;
        return flag1 && flag2 && flag3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.yaw, yaw) == 0 &&
                Double.compare(location.pitch, pitch) == 0 &&
                world.equals(location.world) &&
                position.equals(location.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, position, yaw, pitch);
    }
}