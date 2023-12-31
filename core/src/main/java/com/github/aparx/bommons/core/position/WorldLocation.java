package com.github.aparx.bommons.core.position;

import com.google.errorprone.annotations.CheckReturnValue;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-31 06:10
 * @since 1.0
 */
@CheckReturnValue
@SerializableAs("WorldLocation")
@DefaultQualifier(NonNull.class)
public class WorldLocation extends WorldPosition implements Rot2F {

  static {
    ConfigurationSerialization.registerClass(WorldLocation.class);
  }

  private static final WorldLocation ZERO_LOCATION = new WorldLocation(null, 0, 0, 0, null);

  private static final Rotation NO_ROTATION = new Rotation(0, 0);

  private final Rotation rotation;

  protected WorldLocation(
      @Nullable World world, double posX, double posY, double posZ, @Nullable Rotation rotation) {
    super(world, posX, posY, posZ);
    this.rotation = (rotation != null ? rotation : NO_ROTATION);
  }

  public static WorldLocation of() {
    return ZERO_LOCATION;
  }

  public static WorldLocation of(Location location) {
    return of(location.getWorld(), location.getX(), location.getY(), location.getZ(),
        location.getYaw(), location.getPitch());
  }

  public static WorldLocation of(
      @Nullable World world, Vec3D position, @Nullable Rotation rotation) {
    return of(world, position.getX(), position.getY(), position.getZ(), rotation);
  }

  public static WorldLocation of(
      @Nullable World world, Vec3D position, float yaw, float pitch) {
    return of(world, position.getX(), position.getY(), position.getZ(), yaw, pitch);
  }

  public static WorldLocation of(
      @Nullable World world, Vector vector, @Nullable Rotation rotation) {
    return of(world, vector.getX(), vector.getY(), vector.getZ(), rotation);
  }

  public static WorldLocation of(
      @Nullable World world, Vector vector, float yaw, float pitch) {
    return of(world, vector.getX(), vector.getY(), vector.getZ(), yaw, pitch);
  }

  public static WorldLocation of(
      @Nullable World world, double posX, double posY, double posZ, float yaw, float pitch) {
    return of(world, posX, posY, posZ, new Rotation(yaw, pitch));
  }

  public static WorldLocation of(
      @Nullable World world, double posX, double posY, double posZ, @Nullable Rotation rotation) {
    return new WorldLocation(world, posX, posY, posZ, rotation);
  }

  public static WorldLocation deserialize(Map<?, ?> args) {
    WorldPosition position = WorldPosition.deserialize(args);
    return of(position.findWorld(), position, args.containsKey("yaw")
        ? new Rotation(NumberConversions.toFloat(args.get("yaw")),
        NumberConversions.toFloat(args.get("pitch")))
        : NO_ROTATION);
  }

  @Override
  public Map<String, Object> serialize() {
    Map<String, Object> args = new HashMap<>(super.serialize());
    if (hasRotation()) {
      args.put("yaw", rotation.getYaw());
      args.put("pitch", rotation.getPitch());
    }
    return args;
  }

  @Override
  public float getYaw() {
    return rotation.getYaw();
  }

  @Override
  public float getPitch() {
    return rotation.getPitch();
  }

  @Override
  public WorldLocation setX(double posX) {
    return new WorldLocation(findWorld(), posX, this.posY, this.posZ, rotation);
  }

  @Override
  public WorldLocation setY(double posY) {
    return new WorldLocation(findWorld(), this.posX, posY, this.posZ, rotation);
  }

  @Override
  public WorldLocation setZ(double posZ) {
    return new WorldLocation(findWorld(), this.posX, this.posY, posZ, rotation);
  }

  public boolean hasRotation() {
    return rotation != NO_ROTATION;
  }

  public Rotation getRotation() {
    return rotation;
  }

  public WorldLocation setRotation(Rotation rotation) {
    return new WorldLocation(findWorld(), posX, posY, posZ, rotation);
  }

  public WorldLocation setYaw(float yaw) {
    return setRotation(new Rotation(yaw, rotation.getPitch()));
  }

  public WorldLocation setPitch(float pitch) {
    return setRotation(new Rotation(rotation.getYaw(), pitch));
  }

  @Override
  public WorldLocation add(double x, double y, double z) {
    return new WorldLocation(findWorld(), posX + x, posY + y, posZ + z, rotation);
  }

  @Override
  public WorldLocation add(Vec3D offset) {
    return (WorldLocation) super.add(offset);
  }

  @Override
  public WorldLocation add(Vector vector) {
    return (WorldLocation) super.add(vector);
  }

  @Override
  public WorldLocation add(Location location) {
    return (WorldLocation) super.add(location);
  }

  @Override
  public WorldLocation subtract(double x, double y, double z) {
    return new WorldLocation(findWorld(), posX - x, posY - y, posZ - z, rotation);
  }

  @Override
  public WorldLocation subtract(Vec3D offset) {
    return (WorldLocation) super.subtract(offset);
  }

  @Override
  public WorldLocation subtract(Vector vector) {
    return (WorldLocation) super.subtract(vector);
  }

  @Override
  public WorldLocation subtract(Location location) {
    return (WorldLocation) super.subtract(location);
  }

  @Override
  public WorldLocation multiply(double x, double y, double z) {
    return new WorldLocation(findWorld(), posX * x, posY * y, posZ * z, rotation);
  }

  @Override
  public WorldLocation multiply(Vec3D offset) {
    return (WorldLocation) super.multiply(offset);
  }

  @Override
  public WorldLocation multiply(Vector vector) {
    return (WorldLocation) super.multiply(vector);
  }

  @Override
  public WorldLocation multiply(Location location) {
    return (WorldLocation) super.multiply(location);
  }
}
