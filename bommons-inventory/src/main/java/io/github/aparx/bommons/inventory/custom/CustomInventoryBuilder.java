package io.github.aparx.bommons.inventory.custom;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import io.github.aparx.bommons.inventory.InventoryDimensions;
import io.github.aparx.bommons.inventory.custom.content.InventoryLayerGroup;
import io.github.aparx.bommons.inventory.custom.content.InventoryStorageLayer;
import io.github.aparx.bommons.inventory.custom.content.pagination.InventoryDynamicPageGroup;
import io.github.aparx.bommons.inventory.custom.content.pagination.InventoryPageGroup;
import io.github.aparx.bommons.ticks.TickDuration;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.function.Consumer;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-25 04:53
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public final class CustomInventoryBuilder {

  private String title = "Custom Inventory";
  private @Nullable TickDuration updateInterval;
  private @Nullable InventoryContentView content;

  private CustomInventoryBuilder() {}

  public static CustomInventoryBuilder builder() {
    return new CustomInventoryBuilder();
  }

  @CanIgnoreReturnValue
  public CustomInventoryBuilder title(String title) {
    Preconditions.checkNotNull(title, "Title must not be null");
    this.title = title;
    return this;
  }

  public String getTitle() {
    return title;
  }

  @CanIgnoreReturnValue
  public CustomInventoryBuilder interval(@Nullable TickDuration updateInterval) {
    this.updateInterval = updateInterval;
    return this;
  }

  public @Nullable TickDuration getUpdateInterval() {
    return updateInterval;
  }

  @CanIgnoreReturnValue
  public CustomInventoryBuilder withContent(@Nullable InventoryContentView content) {
    this.content = content;
    return this;
  }

  @CanIgnoreReturnValue
  public CustomInventoryBuilder withLayerContent(
      InventoryDimensions dimensions, @Nullable Consumer<InventoryLayerGroup> populator) {
    InventoryLayerGroup layerGroup = InventoryContentFactory.layerGroup(dimensions);
    if (populator != null) populator.accept(layerGroup);
    return withContent(layerGroup);
  }

  @CanIgnoreReturnValue
  public CustomInventoryBuilder withStorageContent(
      InventoryDimensions dimensions, @Nullable Consumer<InventoryStorageLayer> populator) {
    InventoryStorageLayer storageLayer = InventoryContentFactory.storageLayer(dimensions);
    if (populator != null) populator.accept(storageLayer);
    return withContent(storageLayer);
  }

  @CanIgnoreReturnValue
  public CustomInventoryBuilder withPageGroup(
      InventoryDimensions dimensions, @Nullable Consumer<InventoryPageGroup> populator) {
    InventoryPageGroup pageGroup = InventoryContentFactory.pageGroup(dimensions);
    if (populator != null) populator.accept(pageGroup);
    return withContent(pageGroup);
  }

  @CanIgnoreReturnValue
  public CustomInventoryBuilder withDynamicPageGroup(
      InventoryDimensions dimensions, @Nullable Consumer<InventoryDynamicPageGroup> populator) {
    InventoryDynamicPageGroup pageGroup = InventoryContentFactory.dynamicPageGroup(dimensions);
    if (populator != null) populator.accept(pageGroup);
    return withContent(pageGroup);
  }

  public @Nullable InventoryContentView getContent() {
    return content;
  }

  @CheckReturnValue
  public CustomInventory build(Plugin plugin) {
    CustomInventory inventory = (updateInterval != null
        ? new CustomInventory(plugin, updateInterval, title)
        : new CustomInventory(plugin, title));
    if (content != null) inventory.updateContent(content);
    return inventory;
  }

}
