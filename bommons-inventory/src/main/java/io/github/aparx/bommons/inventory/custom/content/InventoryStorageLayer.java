package io.github.aparx.bommons.inventory.custom.content;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.aparx.bommons.core.utils.IndexMap;
import io.github.aparx.bommons.inventory.InventoryDimensions;
import io.github.aparx.bommons.inventory.InventoryPosition;
import io.github.aparx.bommons.inventory.InventorySection;
import io.github.aparx.bommons.inventory.item.InventoryItem;
import io.github.aparx.bommons.inventory.item.InventoryItemAccessor;
import io.github.aparx.bommons.inventory.custom.InventoryLayerView;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Iterator;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 15:27
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class InventoryStorageLayer implements InventoryLayerView,
    Iterable<@Nullable InventoryItem> {

  private final InventorySection area;

  /** Growable and shrinkable array to avoid memory overhead */
  private final IndexMap<@Nullable InventoryItem> elementsArray = new IndexMap<>();

  public InventoryStorageLayer(InventorySection area) {
    Preconditions.checkNotNull(area, "Area must not be null");
    this.area = area;
  }

  public InventorySection getArea() {
    return area;
  }

  private int calculateElementIndex(InventoryPosition position) {
    // positionalIndex is the absolute position (not the element index!)
    if (!area.includes(position)) return -1;
    InventoryDimensions dimensions = getDimensions();
    int index = position.getIndex() - area.getBegin().getIndex();
    int column = InventoryPosition.toColumn(index, dimensions.getWidth());
    int row = InventoryPosition.toRow(index, dimensions.getWidth());
    return InventoryPosition.toIndex(column, row, dimensions.getWidth());
  }

  public boolean includes(int index) {
    return area.includes(index);
  }

  public boolean includes(InventoryPosition position) {
    return area.includes(position);
  }

  @Override
  public @Nullable InventoryItem get(
      @Nullable InventoryItemAccessor accessor, InventoryPosition position) {
    int elementIndex = calculateElementIndex(position);
    if (elementIndex >= 0 && elementIndex < elementsArray.capacity())
      return elementsArray.get(elementIndex);
    return null;
  }

  @CanIgnoreReturnValue
  public @Nullable InventoryItem set(InventoryPosition position, @Nullable InventoryItem item) {
    int elementIndex = calculateElementIndex(position);
    if (elementIndex < 0)
      throw new IllegalArgumentException("Position is outside the view");
    return elementsArray.put(elementIndex, item);
  }

  @CanIgnoreReturnValue
  public @Nullable InventoryItem set(int elementIndex, @Nullable InventoryItem item) {
    Preconditions.checkElementIndex(elementIndex, area.size());
    return elementsArray.put(elementIndex, item);
  }

  @CanIgnoreReturnValue
  public @Nullable InventoryItem remove(int elementIndex) {
    Preconditions.checkElementIndex(elementIndex, area.size());
    return elementsArray.remove(elementIndex);
  }

  @CanIgnoreReturnValue
  public boolean remove(int elementIndex, @Nullable InventoryItem item) {
    Preconditions.checkElementIndex(elementIndex, area.size());
    return elementsArray.remove(elementIndex, item);
  }

  public void clear() {
    elementsArray.clear();
  }

  @CanIgnoreReturnValue
  public InventoryStorageLayer fill(@Nullable InventoryItem item) {
    final int length = area.size();
    elementsArray.ensureCapacity(length);
    for (int i = 0; i < length; ++i)
      elementsArray.put(i, item);
    return this;
  }

  @CanIgnoreReturnValue
  public InventoryStorageLayer fillEdges(@Nullable InventoryItem item) {
    int height = getDimensions().getHeight();
    if (height >= 1)
      fillTop(item);
    if (height >= 2)
      fillBottom(item);
    return fillSides(item);
  }

  @CanIgnoreReturnValue
  public InventoryStorageLayer fillTop(@Nullable InventoryItem item) {
    for (int i = getDimensions().getWidth(); i > 0; --i)
      elementsArray.put(i - 1, item);
    return this;
  }

  @CanIgnoreReturnValue
  public InventoryStorageLayer fillBottom(@Nullable InventoryItem item) {
    InventoryDimensions dim = getDimensions();
    int width = dim.getWidth();
    int fromIndex = dim.size() - width;
    for (int i = 0; i < width; ++i)
      elementsArray.put(fromIndex + i, item);
    return this;
  }

  @CanIgnoreReturnValue
  public InventoryStorageLayer fillSides(@Nullable InventoryItem item) {
    InventoryDimensions dim = getDimensions();
    int width = dim.getWidth(), height = dim.getHeight();
    for (int row = 0; row < height; ++row) {
      set(InventoryPosition.ofPoint(0, row, width), item);
      set(InventoryPosition.ofPoint(width - 1, row, width), item);
    }
    return this;
  }

  @Override
  public Iterator<@Nullable InventoryItem> iterator() {
    return new Iterator<>() {
      int cursor = 0;

      @Override
      public boolean hasNext() {
        return cursor < Math.min(area.size(), elementsArray.capacity());
      }

      @Override
      public @Nullable InventoryItem next() {
        return elementsArray.get(cursor++);
      }
    };
  }
}
