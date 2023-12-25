package io.github.aparx.bommons.inventory.custom.content.pagination;

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
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;

/**
 * An {@code InventoryContentView} implementation, that has the ability to contain multiple pages
 * that can be displayed as wanted.
 *
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 21:24
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class InventoryPageGroup extends InventoryContentView implements Iterable<InventoryContentView> {

  private static final InventoryItem DEFAULT_PREVIOUS_PAGE = InventoryItemFactory.builder()
      .item(ItemStackBuilders.create(Material.ARROW).name("Previous").build()).cancel().build();

  private static final InventoryItem DEFAULT_NEXT_PAGE = InventoryItemFactory.builder()
      .item(ItemStackBuilders.create(Material.ARROW).name("Next").build()).cancel().build();

  private static final InventoryItem NO_PAGINATION = InventoryItemFactory.builder()
      .item(ItemStackBuilders.create(Material.BLACK_STAINED_GLASS_PANE)
          .name(StringUtils.SPACE).build()).cancel().build();

  private final EnumMap<PaginationItemType, PaginationItem>
      paginationItems = new EnumMap<>(PaginationItemType.class);

  private final ArrayList<InventoryContentView> pages = new ArrayList<>();

  private @Nullable InventoryItem noPaginationItem = NO_PAGINATION;

  private int pageIndex;

  public InventoryPageGroup(InventorySection area, @Nullable InventorySection parent) {
    super(area, parent);
    setPaginationItem(PaginationItemType.PREVIOUS_PAGE,
        fromRelative(InventoryPosition.ofLast(area))
            .shift(1 - area.getDimensions().getWidth()),
        DEFAULT_PREVIOUS_PAGE);
    setPaginationItem(PaginationItemType.NEXT_PAGE,
        fromRelative(InventoryPosition.ofLast(area)),
        DEFAULT_NEXT_PAGE);
  }

  public void setPaginationItem(PaginationItemType type, InventoryItem item) {
    Preconditions.checkNotNull(item, "Item must not be null");
    Preconditions.checkState(paginationItems.containsKey(type));
    setPaginationItem(type, paginationItems.get(type).getAbsolutePosition(), item);
  }

  public void setPaginationItem(
      PaginationItemType type, InventoryPosition absolutePosition, InventoryItem item) {
    Preconditions.checkNotNull(item, "Item must not be null");
    Preconditions.checkNotNull(absolutePosition, "Position must not be null");
    Preconditions.checkArgument(getArea().includes(absolutePosition), "Position is outside group " +
        "area");
    paginationItems.put(type, new PaginationItem(type, absolutePosition,
        InventoryItemFactory.builder(item)
            .addClickHandle((event) -> paginate(type.getSkipType(), 1))
            .build()));
  }

  public PaginationItem getPaginationItem(PaginationItemType type) {
    return paginationItems.get(type);
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
    for (PaginationItemType type : PaginationItemType.values()) {
      PaginationItem paginationItem = paginationItems.get(type);
      if (position.getIndex() == paginationItem.getAbsolutePosition().getIndex())
        return (has(type.getSkipType()) ? paginationItem.getItem() : noPaginationItem);
    }
    @Nullable InventoryContentView page = getPage();
    return (page != null ? page.get(accessor, position) : null);
  }

  @CanIgnoreReturnValue
  public boolean paginate(int toIndex) {
    if (toIndex < 0 || toIndex >= pages.size())
      return false;
    this.pageIndex = toIndex;
    // TODO force re-render?
    return true;
  }

  @CanIgnoreReturnValue
  public boolean paginate(PaginationSkipType type, int amount) {
    int newPageIndex = Math.max(0, Math.min(pageIndex + amount * type.getFactor(), pages.size()));
    return pageIndex != newPageIndex && paginate(newPageIndex);
  }

  public boolean has(PaginationSkipType type) {
    switch (type) {
      case NEXT:
        return hasNextPage();
      case PREVIOUS:
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

  @Override
  public Iterator<InventoryContentView> iterator() {
    return new Iterator<>() {

      int cursor = 0;

      @Override
      public boolean hasNext() {
        return cursor < pages.size();
      }

      @Override
      public InventoryContentView next() {
        return getPage(cursor++);
      }
    };
  }

  public static class PaginationItem {
    private final PaginationItemType type;
    /** The position absolutely aligned (to the parent) */
    private final InventoryPosition absolutePosition;
    private final InventoryItem item;

    public PaginationItem(
        PaginationItemType type,
        InventoryPosition absolutePosition,
        InventoryItem item) {
      Preconditions.checkNotNull(type, "Type must not be null");
      Preconditions.checkNotNull(absolutePosition, "Position must not be null");
      Preconditions.checkNotNull(item, "Item must not be null");
      this.type = type;
      this.absolutePosition = absolutePosition;
      this.item = item;
    }

    public PaginationItemType getType() {
      return type;
    }

    public InventoryPosition getAbsolutePosition() {
      return absolutePosition;
    }

    public InventoryItem getItem() {
      return item;
    }
  }
}
