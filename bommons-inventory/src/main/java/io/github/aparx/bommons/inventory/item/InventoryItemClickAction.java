package io.github.aparx.bommons.inventory.item;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 15:04
 * @since 1.0
 */
@FunctionalInterface
public interface InventoryItemClickAction {

  InventoryItemClickAction CANCELLING = (e) -> e.setCancelled(true);

  void handleClick(@NonNull InventoryClickEvent event);

  default InventoryItemClickAction andThen(InventoryItemClickAction action) {
    return (action != null ? (event) -> {
      this.handleClick(event);
      action.handleClick(event);
    } : this);
  }

}
