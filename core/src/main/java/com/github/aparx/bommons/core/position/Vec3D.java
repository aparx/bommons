package com.github.aparx.bommons.core.position;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-31 05:43
 * @since 1.0
 */
public interface Vec3D {

  /** Returns the x-coordinate */
  double getX();

  /** Returns the y-coordinate */
  double getY();

  /** Returns the z-coordinate */
  double getZ();

  static Vec3D of(double x, double y, double z) {
    return new Vec3D() {
      @Override
      public double getX() {
        return x;
      }

      @Override
      public double getY() {
        return y;
      }

      @Override
      public double getZ() {
        return z;
      }
    };
  }

  static Vec3D of(Vector vector) {
    return of(vector.getX(), vector.getY(), vector.getZ());
  }

  static Vec3D of(Location location) {
    return of(location.getX(), location.getY(), location.getZ());
  }

}
