package com.github.aparx.bommons.core.position;

import com.google.errorprone.annotations.CheckReturnValue;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.NumberConversions;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-31 06:03
 * @since 1.0
 */
@CheckReturnValue
@SerializableAs("Rotation")
@DefaultQualifier(NonNull.class)
public class Rotation implements Rot2F, ConfigurationSerializable {

  static {
    ConfigurationSerialization.registerClass(Rotation.class);
  }

  public static Rotation ZERO_ROTATION = new Rotation(0, 0);

  private final float yaw, pitch;

  protected Rotation(float yaw, float pitch) {
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public static Rotation of() {
    return ZERO_ROTATION;
  }

  public static Rotation of(float yaw, float pitch) {
    return new Rotation(yaw, pitch);
  }

  public static Rotation ofYaw(float yaw) {
    return new Rotation(yaw, 0.0F);
  }

  public static Rotation ofPitch(float pitch) {
    return new Rotation(0.0F, pitch);
  }

  public static Rotation deserialize(Map<?, ?> args) {
    return new Rotation(NumberConversions.toFloat(args.get("yaw")),
        NumberConversions.toFloat(args.get("pitch")));
  }

  @Override
  public Map<String, Object> serialize() {
    return Map.of("yaw", yaw, "pitch", pitch);
  }

  @Override
  public float getYaw() {
    return yaw;
  }

  @Override
  public float getPitch() {
    return pitch;
  }

  public Rotation add(float yaw, float pitch) {
    return new Rotation(this.yaw + yaw, this.pitch + pitch);
  }

  /** @see #add(float, float) */
  public Rotation add(float yaw) {
    return add(yaw, 0.0F);
  }

  /** @see #add(float, float) */
  public Rotation add(Rotation rotation) {
    return add(rotation.getYaw(), rotation.getPitch());
  }

  public Rotation subtract(float yaw, float pitch) {
    return new Rotation(this.yaw - yaw, this.pitch - pitch);
  }

  /** @see #subtract(float, float) */
  public Rotation subtract(float yaw) {
    return subtract(yaw, 0.0F);
  }

  /** @see #subtract(float, float) */
  public Rotation subtract(Rotation rotation) {
    return subtract(rotation.getYaw(), rotation.getPitch());
  }

  public Rotation multiply(float yaw, float pitch) {
    return new Rotation(this.yaw * yaw, this.pitch * pitch);
  }

  /** @see #multiply(float, float) */
  public Rotation multiply(float yaw) {
    return multiply(yaw, 1.0F);
  }

  /** @see #multiply(float, float) */
  public Rotation multiply(Rotation rotation) {
    return multiply(rotation.getYaw(), rotation.getPitch());
  }

}
