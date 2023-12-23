package io.github.aparx.bommons.inventory.custom.content;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.aparx.bommons.inventory.InventoryPosition;
import io.github.aparx.bommons.inventory.InventorySection;
import io.github.aparx.bommons.inventory.custom.InventoryContentView;
import io.github.aparx.bommons.inventory.item.InventoryItem;
import io.github.aparx.bommons.inventory.item.InventoryItemAccessor;
import io.github.aparx.bommons.inventory.item.InventoryItemFactory;
import io.github.aparx.bommons.item.ItemStackBuilders;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * An {@code InventoryContentView} implementation, that has the ability to contain multiple pages
 * that can be displayed as wanted.
 *
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 21:24
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class InventoryPagesGroup extends InventoryContentView {

  private static final InventoryItem DEFAULT_PREVIOUS_PAGE = InventoryItemFactory.builder()
      .item(ItemStackBuilders.create(Material.ARROW).name("Previous").build()).cancel().build();

  private static final InventoryItem DEFAULT_NEXT_PAGE = InventoryItemFactory.builder()
      .item(ItemStackBuilders.create(Material.ARROW).name("Next").build()).cancel().build();

  private static final InventoryItem NO_PAGINATION = InventoryItemFactory.builder()
      .item(ItemStackBuilders.create(Material.BLACK_STAINED_GLASS_PANE)
          .name(StringUtils.SPACE).build()).cancel().build();

  private final EnumMap<PaginationType, Pair<InventoryPosition, InventoryItem>>
      paginationItems = new EnumMap<>(PaginationType.class);

  private final ArrayList<InventoryContentView> pages = new ArrayList<>();

  private @Nullable InventoryItem noPaginationItem = NO_PAGINATION;

  private int pageIndex;

  public InventoryPagesGroup(InventorySection parent, InventorySection area) {
    super(area, parent);
    setPaginationItem(PaginationType.PREVIOUS_PAGE,
        fromRelative(InventoryPosition.ofLast(area))
            .shift(1 - area.getDimensions().getWidth()),
        DEFAULT_PREVIOUS_PAGE);
    setPaginationItem(PaginationType.NEXT_PAGE,
        fromRelative(InventoryPosition.ofLast(area)),
        DEFAULT_NEXT_PAGE);
  }

  public void setPaginationItem(PaginationType type, InventoryItem item) {
    Preconditions.checkNotNull(item, "Item must not be null");
    Preconditions.checkState(paginationItems.containsKey(type));
    setPaginationItem(type, paginationItems.get(type).getKey(), item);
  }

  public void setPaginationItem(PaginationType type, InventoryPosition pos, InventoryItem item) {
    Preconditions.checkNotNull(item, "Item must not be null");
    Preconditions.checkNotNull(pos, "Position must not be null");
    Preconditions.checkArgument(getArea().includes(pos), "Position is outside group area");
    paginationItems.put(type, Pair.of(pos, InventoryItemFactory.builder(item)
        .addClickHandle((event) -> paginate(type, 1))
        .build()));
  }

  public void setNoPaginationItem(@Nullable InventoryItem noPaginationItem) {
    this.noPaginationItem = noPaginationItem;
  }

  public @Nullable InventoryItem getNoPaginationItem() {
    return noPaginationItem;
  }

  @Override
  public @Nullable InventoryItem get(
      @Nullable InventoryItemAccessor accessor, InventoryPosition position) {
    for (PaginationType type : PaginationType.values()) {
      Pair<InventoryPosition, InventoryItem> paginationItemPair = paginationItems.get(type);
      if (position.getIndex() == paginationItemPair.getKey().getIndex())
        return (has(type) ? paginationItemPair.getValue() : noPaginationItem);
    }
    @Nullable InventoryContentView page = getPage();
    return (page != null ? page.get(accessor, position) : null);
  }

  @CanIgnoreReturnValue
  public boolean paginate(int toIndex) {
    if (toIndex < 0 || toIndex >= pages.size())
      return false;
    this.pageIndex = toIndex;
    return true;
  }

  @CanIgnoreReturnValue
  public boolean paginate(PaginationType type, int amount) {
    int newPageIndex = Math.max(0, Math.min(pageIndex + amount * type.factor, pages.size()));
    return pageIndex != newPageIndex && paginate(newPageIndex);
  }

  public boolean has(PaginationType type) {
    switch (type) {
      case NEXT_PAGE:
        return hasNextPage();
      case PREVIOUS_PAGE:
        return hasPreviousPage();
      default:
        return false;
    }
  }

  public boolean hasNextPage() {
    return pageIndex < pages.size() - 1;
  }

  public boolean hasPreviousPage() {
    return pageIndex > 0 && !pages.isEmpty();
  }

  public void clear() {
    pages.clear();
  }

  @CanIgnoreReturnValue
  public int addPage(InventoryContentView page) {
    Preconditions.checkArgument(getArea().includes(page.getArea()), "Page is larger than parent");
    int index = 1 + pages.size();
    pages.add(page);
    return index;
  }

  public InventoryContentView getPage(int index) {
    Preconditions.checkElementIndex(index, pages.size());
    return pages.get(index);
  }

  public @Nullable InventoryContentView getPage() {
    if (pageIndex >= 0 && pageIndex < pages.size())
      return getPage(pageIndex);
    return null;
  }

  public int getPageCount() {
    return pages.size();
  }

  public int getPageIndex() {
    return pageIndex;
  }

  public enum PaginationType {
    PREVIOUS_PAGE(-1),
    NEXT_PAGE(1);

    private final int factor;

    PaginationType(int factor) {
      this.factor = factor;
    }

    public int getFactor() {
      return factor;
    }
  }
}
