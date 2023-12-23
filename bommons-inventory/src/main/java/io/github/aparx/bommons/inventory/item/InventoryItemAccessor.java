package io.github.aparx.bommons.inventory.item;

import io.github.aparx.bommons.ticks.ticker.Ticker;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 15:03
 * @since 1.0
 */
public interface InventoryItemAccessor {

  Ticker getUpdateTicker();

}
