package com.github.aparx.bommons.core.position;

import com.google.errorprone.annotations.CheckReturnValue;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BlockVector;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-31 05:43
 * @since 1.0
 */
@CheckReturnValue
@SerializableAs("Position")
@DefaultQualifier(NonNull.class)
public class Position implements Vec3D, ConfigurationSerializable {

  static {
    ConfigurationSerialization.registerClass(Position.class);
  }

  public static final Position ZERO_POSITION = new Position(0, 0, 0);

  protected final double posX, posY, posZ;

  protected Position(double posX, double posY, double posZ) {
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
  }

  public static Position of() {
    return ZERO_POSITION;
  }

  public static Position of(Vec3D position) {
    return new Position(position.getX(), position.getY(), position.getZ());
  }

  public static Position of(Vector vector) {
    return new Position(vector.getX(), vector.getY(), vector.getZ());
  }

  public static Position of(double posX, double posY, double posZ) {
    return new Position(posX, posY, posZ);
  }

  public static Position deserialize(Map<?, ?> args) {
    return new Position(
        NumberConversions.toDouble(args.get("x")),
        NumberConversions.toDouble(args.get("y")),
        NumberConversions.toDouble(args.get("z"))
    );
  }

  @Override
  public Map<String, Object> serialize() {
    return Map.of("x", posX, "y", posY, "z", posZ);
  }

  @Override
  public final double getX() {
    return posX;
  }

  @Override
  public final double getY() {
    return posY;
  }

  @Override
  public final double getZ() {
    return posZ;
  }

  public int getBlockX() {
    return NumberConversions.floor(posX);
  }

  public int getBlockY() {
    return NumberConversions.floor(posY);
  }

  public int getBlockZ() {
    return NumberConversions.floor(posZ);
  }

  public Position setX(double posX) {
    return new Position(posX, this.posY, this.posZ);
  }

  public Position setY(double posY) {
    return new Position(this.posX, posY, this.posZ);
  }

  public Position setZ(double posZ) {
    return new Position(this.posX, this.posY, posZ);
  }

  public Location toLocation(@Nullable World world) {
    return new Location(world, posX, posY, posZ);
  }

  public Vector toVector() {
    return new Vector(posX, posY, posZ);
  }

  public BlockVector toBlockVector() {
    return new BlockVector(getBlockX(), getBlockY(), getBlockZ());
  }

  public Block getBlock(World world) {
    return world.getBlockAt(getBlockX(), getBlockY(), getBlockZ());
  }

  // +--------------------- Arithmetic ---------------------+

  public Position add(double x, double y, double z) {
    return new Position(posX + x, posY + y, posZ + z);
  }

  /** @see #add(double, double, double) */
  public Position add(Vec3D offset) {
    return add(offset.getX(), offset.getY(), offset.getZ());
  }

  /** @see #add(double, double, double) */
  public Position add(Vector vector) {
    return add(vector.getX(), vector.getY(), vector.getZ());
  }

  /** @see #add(double, double, double) */
  public Position add(Location location) {
    return add(location.getX(), location.getY(), location.getZ());
  }

  public Position subtract(double x, double y, double z) {
    return new Position(posX - x, posY - y, posZ - z);
  }

  /** @see #subtract(double, double, double) */
  public Position subtract(Vec3D offset) {
    return subtract(offset.getX(), offset.getY(), offset.getZ());
  }

  /** @see #subtract(double, double, double) */
  public Position subtract(Vector vector) {
    return subtract(vector.getX(), vector.getY(), vector.getZ());
  }

  /** @see #subtract(double, double, double) */
  public Position subtract(Location location) {
    return subtract(location.getX(), location.getY(), location.getZ());
  }

  public Position multiply(double x, double y, double z) {
    return new Position(posX * x, posY * y, posZ * z);
  }

  /** @see #multiply(double, double, double) */
  public Position multiply(Vec3D offset) {
    return multiply(offset.getX(), offset.getY(), offset.getZ());
  }

  /** @see #multiply(double, double, double) */
  public Position multiply(Vector vector) {
    return multiply(vector.getX(), vector.getY(), vector.getZ());
  }

  /** @see #multiply(double, double, double) */
  public Position multiply(Location location) {
    return multiply(location.getX(), location.getY(), location.getZ());
  }

}
