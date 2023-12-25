package io.github.aparx.bommons.inventory.custom.content.pagination;

import io.github.aparx.bommons.inventory.InventoryPosition;
import io.github.aparx.bommons.inventory.InventorySection;
import io.github.aparx.bommons.inventory.custom.InventoryContentView;
import io.github.aparx.bommons.inventory.custom.content.InventoryStorageLayer;
import io.github.aparx.bommons.inventory.item.InventoryItem;
import io.github.aparx.bommons.inventory.item.InventoryItemAccessor;
import org.apache.commons.lang3.ArrayUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-25 03:12
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class InventoryDynamicPageGroup extends InventoryContentView {

  private final transient Object lock = new Object();

  private final InventoryPageGroup group;
  private final List<@Nullable InventoryItem> elements = new ElementList();

  private volatile boolean updatePages;

  public InventoryDynamicPageGroup(InventorySection area, @Nullable InventorySection parentArea) {
    this(new InventoryPageGroup(area, parentArea));
  }

  public InventoryDynamicPageGroup(InventoryPageGroup pagesGroup) {
    super(pagesGroup.getArea(), pagesGroup.getParent());
    this.group = pagesGroup;
  }

  @Override
  public @Nullable InventoryItem get(
      @Nullable InventoryItemAccessor accessor, InventoryPosition position) {
    synchronized (lock) {
      try {
        if (updatePages) recreatePages();
        return group.get(accessor, position);
      } finally {
        // we have to ensure `updatePages` is false, even for when an exception is thrown
        updatePages = false;
      }
    }
  }

  public InventoryPageGroup getGroup() {
    return group;
  }

  public int getMaximumElementSizePerPage() {
    return group.getArea().size() - PaginationItemType.values().length;
  }

  public int getExpectedNumberOfPages(int maxPerPage) {
    return (int) Math.ceil(elements.size() / (double) maxPerPage);
  }

  public int getExpectedNumberOfPages() {
    return getExpectedNumberOfPages(getMaximumElementSizePerPage());
  }

  public void recreatePages() {
    recreatePages(getMaximumElementSizePerPage());
  }

  /**
   * Recreates all pages to the expected amount of pages.
   *
   * @param maxPerPage the maximum number of elements per page
   */
  private void recreatePages(int maxPerPage) {
    synchronized (lock) {
      final int elemSize = elements.size();
      group.clear();
      for (int i = elemSize + maxPerPage, pageIdx = 0; (i -= maxPerPage) > 0; ++pageIdx)
        group.addPage(createPage(pageIdx, elements.subList(pageIdx * maxPerPage,
            Math.min((1 + pageIdx) * maxPerPage, elemSize))));
    }
  }

  /**
   * Creates a new page at page index {@code pageIndex} with given {@code elements}.
   * <p>The {@code elements} list-size should not exceed the maximum capacity of a page, nor
   * should it be more than the capacity of the page minus the amount of maximum pagination items
   * displayable (if set in the same area, as the elements are displayed in).
   *
   * @param pageIndex the index of the page (zero-indexed)
   * @param elements  the elements to be added to that page
   * @return the page for {@code pageIndex}
   */
  protected InventoryContentView createPage(int pageIndex, List<@Nullable InventoryItem> elements) {
    InventoryStorageLayer storageLayer = new InventoryStorageLayer(group.getArea(),
        group.getParent());
    int[] excludeIndices = getIndicesFromPaginationItems();
    int insertIndex = 0;
    for (InventoryItem element : elements) {
      // skip all indices that involve any of the pagination items
      while (ArrayUtils.contains(excludeIndices, insertIndex))
        ++insertIndex;
      storageLayer.set(insertIndex, element);
      ++insertIndex;
    }
    return storageLayer;
  }

  /**
   * Returns an array of all (relative) element indices at which pagination items are stored. These
   * indices should be ignored (as in left out) when filling a page with elements.
   *
   * @return an array of relative indices at which pagination items are kept
   */
  protected int[] getIndicesFromPaginationItems() {
    PaginationItemType[] types = PaginationItemType.values();
    int[] excludeIndices = new int[types.length];
    for (int i = 0; i < types.length; ++i)
      excludeIndices[i] = group.getPaginationItem(types[i])
          .getAbsolutePosition().relative(getArea()).getIndex();
    return excludeIndices;
  }

  /**
   * Returns a mutable list of elements.
   * <p>Changes made in the returning list will reflect changes in this content view.
   * <p>Adding and removing from the below list leads to a forced re-creation of all pages.
   *
   * @return all elements from this group
   */
  public List<@Nullable InventoryItem> getElements() {
    return elements;
  }

  private class ElementList extends ArrayList<@Nullable InventoryItem> {

    @Override
    public boolean add(@Nullable InventoryItem item) {
      synchronized (lock) {
        if (!super.add(item))
          return false;
        return updatePages = true;
      }
    }

    @Override
    public void add(int index, @Nullable InventoryItem element) {
      synchronized (lock) {
        super.add(index, element);
        updatePages = true;
      }
    }

    @Override
    public boolean remove(Object o) {
      synchronized (lock) {
        if (!super.remove(o))
          return false;
        return updatePages = true;
      }
    }

    @Override
    public @Nullable InventoryItem remove(int index) {
      synchronized (lock) {
        @Nullable InventoryItem item = super.remove(index);
        updatePages |= item != null;
        return item;
      }
    }

    @Override
    public boolean addAll(Collection<? extends @Nullable InventoryItem> c) {
      synchronized (lock) {
        if (!super.addAll(c))
          return false;
        return updatePages = true;
      }
    }

    @Override
    public boolean addAll(int index, Collection<? extends @Nullable InventoryItem> c) {
      synchronized (lock) {
        if (!super.addAll(index, c))
          return false;
        return updatePages = true;
      }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      synchronized (lock) {
        if (!super.removeAll(c))
          return false;
        return updatePages = true;
      }
    }

    @Override
    public boolean removeIf(Predicate<? super @Nullable InventoryItem> filter) {
      synchronized (lock) {
        if (!super.removeIf(filter))
          return false;
        return updatePages = true;
      }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      synchronized (lock) {
        if (!super.retainAll(c))
          return false;
        return updatePages = true;
      }
    }

  }

}
