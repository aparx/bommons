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
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 15:25
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public abstract class InventoryContentView {

  private final @Nullable InventorySection parent;
  private final InventorySection area;

  public InventoryContentView(InventorySection area, @Nullable InventorySection parent) {
    Preconditions.checkNotNull(area, "Area must not be null");
    this.parent = parent;
    this.area = area;
  }

  /**
   * Returns the area of {@code view}, or null if it is null.
   *
   * @param view the view to get the area from
   * @return null if {@code view} is null, otherwise it's area
   * @see InventoryContentView#getArea()
   */
  public static @Nullable InventorySection getArea(@Nullable InventoryContentView view) {
    return (view != null ? view.getArea() : null);
  }

  public abstract @Nullable InventoryItem get(
      @Nullable InventoryItemAccessor accessor, InventoryPosition position);

  public final InventorySection getArea() {
    return area;
  }

  @Deterministic
  public final @NonNull InventoryDimensions getDimensions() {
    return getArea().getDimensions();
  }

  public final boolean hasParent() {
    return parent != null;
  }

  public @Nullable InventorySection getParent() {
    return parent;
  }

  /**
   * Flattens (non-relative) {@code position} to a sequential element index relative to this area.
   *
   * @param position the (non-relative!) view to map to an element index
   * @return the element index, or {@code -1} if {@code position} lies outside this area
   */
  public int toAreaElementIndex(InventoryPosition position) {
    // positionalIndex is the absolute position (not the element index!)
    if (!getArea().includes(position)) return -1;
    InventoryPosition begin = area.getBegin();
    return (position.getIndex() - begin.getIndex())
        - (position.getRow() - begin.getRow())
        * (position.getWidth() - getDimensions().getWidth());
  }

  /**
   * Returns {@code position}, but relative to this {@code area} and shifted by an offset equal
   * to this area's beginning index.
   * <p>This is useful to create an independent {@code InventoryPosition} that can be used by the
   * outside, that references a relative position within this view.
   * <p>Assuming the {@code area} begins at {@code [1, 1]} and ends at {@code [3, 3]},
   * following examples can be used:
   * <ul>
   *   <li>{@code fromRelative(InventoryPosition.ofFirst()) ::= [1, 1]}</li>
   *   <li>{@code fromRelative(InventoryPosition.ofPoint(0, 0)) ::= [1, 1]}</li>
   *   <li>{@code fromRelative(InventoryPosition.ofLast(getArea())) ::= [3, 3]}</li>
   *   <li>{@code fromRelative(InventoryPosition.ofPoint(2, 2)) ::= [3, 3]}</li>
   *   <li>{@code fromRelative(InventoryPosition.ofPoint(1, 0)) ::= [2, 1]}</li>
   *   <li>{@code fromRelative(InventoryPosition.ofPoint(2, 0)) ::= [3, 1]}</li>
   *   <li>{@code fromRelative(InventoryPosition.ofPoint(3, 0)) ::= [4, 1]} <b>(outside)</b></li>
   *   <li>{@code fromRelative(InventoryPosition.ofPoint(0, 1)) ::= [1, 2]}</li>
   * </ul>
   *
   * @param position the relative position to transform into a normal position
   * @return the new non-relative position
   */
  public InventoryPosition fromRelative(InventoryPosition position) {
    return (parent != null ? position.relative(parent) : position).shift(area.getBegin().getIndex());
  }

}
