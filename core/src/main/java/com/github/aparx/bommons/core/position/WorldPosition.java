package com.github.aparx.bommons.core.position;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CheckReturnValue;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-31 05:52
 * @since 1.0
 */
@CheckReturnValue
@SerializableAs("WorldPosition")
@DefaultQualifier(NonNull.class)
public class WorldPosition extends Position {

  static {
    ConfigurationSerialization.registerClass(WorldPosition.class);
  }

  private static final WorldPosition ZERO_POSITION = new WorldPosition(null, 0, 0, 0);

  private final @Nullable Reference<World> world;

  protected WorldPosition(@Nullable World world, double posX, double posY, double posZ) {
    super(posX, posY, posZ);
    this.world = (world != null ? new WeakReference<>(world) : null);
  }

  public static WorldPosition of() {
    return ZERO_POSITION;
  }

  public static WorldPosition of(Location location) {
    return of(location.getWorld(), location.getX(), location.getY(), location.getZ());
  }

  public static WorldPosition of(@Nullable World world, Vec3D position) {
    return of(world, position.getX(), position.getY(), position.getZ());
  }

  public static WorldPosition of(@Nullable World world, Vector vector) {
    return of(world, vector.getX(), vector.getY(), vector.getZ());
  }

  public static WorldPosition of(@Nullable World world, double posX, double posY, double posZ) {
    return new WorldPosition(world, posX, posY, posZ);
  }

  public static WorldPosition deserialize(Map<?, ?> args) {
    return WorldPosition.of(
        Bukkit.getWorld(Objects.toString(args.get("world"), StringUtils.EMPTY)),
        NumberConversions.toDouble(args.get("x")),
        NumberConversions.toDouble(args.get("y")),
        NumberConversions.toDouble(args.get("z"))
    );
  }

  @Override
  public Map<String, Object> serialize() {
    Map<String, Object> args = new HashMap<>(super.serialize());
    @Nullable World world = findWorld();
    if (world != null) args.put("world", world.getName());
    return args;
  }

  public World getWorld() {
    Preconditions.checkNotNull(world, "World is not defined");
    @Nullable World world = this.world.get();
    Preconditions.checkState(world != null, "World has become invalid");
    return world;
  }

  public boolean isWorldLoaded() {
    if (world == null) return false;
    @Nullable World world = this.world.get();
    return world != null && Bukkit.getWorld(world.getUID()) != null;
  }

  public @Nullable World findWorld() {
    return (world != null ? world.get() : null);
  }

  @Override
  public WorldPosition setX(double posX) {
    return new WorldPosition(findWorld(), posX, this.posY, this.posZ);
  }

  @Override
  public WorldPosition setY(double posY) {
    return new WorldPosition(findWorld(), this.posX, posY, this.posZ);
  }

  @Override
  public WorldPosition setZ(double posZ) {
    return new WorldPosition(findWorld(), this.posX, this.posY, posZ);
  }

  public Block getBlock() {
    return getBlock(getWorld());
  }

  public Chunk getChunk() {
    return getWorld().getChunkAt(getBlockX(), getBlockZ());
  }

  public Location toLocation() {
    return toLocation(getWorld());
  }

  @Override
  public WorldPosition add(double x, double y, double z) {
    return new WorldPosition(findWorld(), posX + x, posY + y, posZ + z);
  }

  @Override
  public WorldPosition add(Vec3D offset) {
    return (WorldPosition) super.add(offset);
  }

  @Override
  public WorldPosition add(Vector vector) {
    return (WorldPosition) super.add(vector);
  }

  @Override
  public WorldPosition add(Location location) {
    return (WorldPosition) super.add(location);
  }

  @Override
  public WorldPosition subtract(double x, double y, double z) {
    return new WorldPosition(findWorld(), posX - x, posY - y, posZ - z);
  }

  @Override
  public WorldPosition subtract(Vec3D offset) {
    return (WorldPosition) super.subtract(offset);
  }

  @Override
  public WorldPosition subtract(Vector vector) {
    return (WorldPosition) super.subtract(vector);
  }

  @Override
  public WorldPosition subtract(Location location) {
    return (WorldPosition) super.subtract(location);
  }

  @Override
  public WorldPosition multiply(double x, double y, double z) {
    return new WorldPosition(findWorld(), posX * x, posY * y, posZ * z);
  }

  @Override
  public WorldPosition multiply(Vec3D offset) {
    return (WorldPosition) super.multiply(offset);
  }

  @Override
  public WorldPosition multiply(Vector vector) {
    return (WorldPosition) super.multiply(vector);
  }

  @Override
  public WorldPosition multiply(Location location) {
    return (WorldPosition) super.multiply(location);
  }
}
