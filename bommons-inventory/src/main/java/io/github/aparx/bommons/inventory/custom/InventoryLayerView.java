package io.github.aparx.bommons.inventory.custom;

import com.google.common.base.Preconditions;
import io.github.aparx.bommons.inventory.InventoryDimensions;
import io.github.aparx.bommons.inventory.InventoryPosition;
import io.github.aparx.bommons.inventory.InventorySection;
import io.github.aparx.bommons.inventory.item.InventoryItem;
import io.github.aparx.bommons.inventory.item.InventoryItemAccessor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Deterministic;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 15:25
 * @since 1.0
 */
public interface InventoryLayerView {

  @Deterministic
  @NonNull InventorySection getArea();

  @Nullable InventoryItem get(@Nullable InventoryItemAccessor accessor, InventoryPosition position);

  @Deterministic
  default @NonNull InventoryDimensions getDimensions() {
    return getArea().getDimensions();
  }

  static InventoryLayerView empty(@NonNull InventorySection section) {
    Preconditions.checkNotNull(section, "Dimensions must not be null");
    return new InventoryLayerView() {

      @Override
      public @NonNull InventorySection getArea() {
        return section;
      }

      @Override
      public @Nullable InventoryItem get(
          InventoryItemAccessor accessor, InventoryPosition position) {
        return null;
      }
    };
  }

}
