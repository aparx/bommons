package io.github.aparx.bommons.item;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Supplier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 15:10
 * @since 1.0
 */
@FunctionalInterface
public interface ItemStackSupplier {

  ItemStack getItemStack();

  static ItemStackSupplier of(@Nullable Supplier<ItemStack> supplier) {
    return (supplier != null ? supplier::get : () -> null);
  }

  static ItemStackSupplier of(@Nullable ItemStack itemStack) {
    return () -> itemStack;
  }

}
