package io.github.aparx.bommons.inventory.custom;

import io.github.aparx.bommons.inventory.InventoryDimensions;
import io.github.aparx.bommons.inventory.InventorySection;
import io.github.aparx.bommons.inventory.custom.content.InventoryLayerGroup;
import io.github.aparx.bommons.inventory.custom.content.InventoryStorageLayer;
import io.github.aparx.bommons.inventory.custom.content.pagination.InventoryDynamicPageGroup;
import io.github.aparx.bommons.inventory.custom.content.pagination.InventoryPageGroup;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-25 04:56
 * @since 1.0
 */
public final class InventoryContentFactory {

  private InventoryContentFactory() {
    throw new AssertionError();
  }

  // +------------------ InventoryLayerGroup ------------------+

  public static InventoryLayerGroup layerGroup(
      InventorySection area, @Nullable InventoryContentView parent) {
    return new InventoryLayerGroup(area, InventoryContentView.getArea(parent));
  }

  public static InventoryLayerGroup layerGroup(InventorySection area) {
    return layerGroup(area, null);
  }

  public static InventoryLayerGroup layerGroup(
      InventoryDimensions dimensions, @Nullable InventoryContentView parent) {
    return layerGroup(InventorySection.of(dimensions), parent);
  }

  public static InventoryLayerGroup layerGroup(InventoryDimensions dimensions) {
    return layerGroup(dimensions, null);
  }

  public static InventoryLayerGroup layerGroup(InventoryContentView parent) {
    return layerGroup(parent.getArea(), parent);
  }

  // +------------------ InventoryStorageLayer ------------------+

  public static InventoryStorageLayer storageLayer(
      InventorySection area, @Nullable InventoryContentView parent) {
    return new InventoryStorageLayer(area, InventoryContentView.getArea(parent));
  }

  public static InventoryStorageLayer storageLayer(InventorySection area) {
    return storageLayer(area, null);
  }

  public static InventoryStorageLayer storageLayer(
      InventoryDimensions dimensions, @Nullable InventoryContentView parent) {
    return storageLayer(InventorySection.of(dimensions), parent);
  }

  public static InventoryStorageLayer storageLayer(InventoryDimensions dimensions) {
    return storageLayer(dimensions, null);
  }

  public static InventoryStorageLayer storageLayer(InventoryContentView parent) {
    return storageLayer(parent.getArea(), parent);
  }

  // +------------------ InventoryPageGroup ------------------+

  public static InventoryPageGroup pageGroup(
      InventorySection area, @Nullable InventoryContentView parent) {
    return new InventoryPageGroup(area, InventoryContentView.getArea(parent));
  }

  public static InventoryPageGroup pageGroup(InventorySection area) {
    return pageGroup(area, null);
  }

  public static InventoryPageGroup pageGroup(
      InventoryDimensions dimensions, @Nullable InventoryContentView parent) {
    return pageGroup(InventorySection.of(dimensions), parent);
  }

  public static InventoryPageGroup pageGroup(InventoryDimensions dimensions) {
    return pageGroup(dimensions, null);
  }

  public static InventoryPageGroup pageGroup(InventoryContentView parent) {
    return pageGroup(parent.getArea(), parent);
  }

  // +------------------ InventoryDynamicPageGroup ------------------+

  public static InventoryDynamicPageGroup dynamicPageGroup(
      InventorySection area, @Nullable InventoryContentView parent) {
    return new InventoryDynamicPageGroup(area, InventoryContentView.getArea(parent));
  }

  public static InventoryDynamicPageGroup dynamicPageGroup(InventorySection area) {
    return dynamicPageGroup(area, null);
  }

  public static InventoryDynamicPageGroup dynamicPageGroup(
      InventoryDimensions dimensions, @Nullable InventoryContentView parent) {
    return dynamicPageGroup(InventorySection.of(dimensions), parent);
  }

  public static InventoryDynamicPageGroup dynamicPageGroup(InventoryDimensions dimensions) {
    return dynamicPageGroup(dimensions, null);
  }

  public static InventoryDynamicPageGroup dynamicPageGroup(InventoryContentView parent) {
    return dynamicPageGroup(parent.getArea(), parent);
  }

  public static InventoryDynamicPageGroup dynamicPageGroup(InventoryPageGroup group) {
    return new InventoryDynamicPageGroup(group);
  }
}
