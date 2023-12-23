package io.github.aparx.bommons.inventory.item;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import io.github.aparx.bommons.item.ItemStackSupplier;
import io.github.aparx.bommons.item.WrappedItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.function.Function;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 15:05
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public final class InventoryItemFactory {

  private InventoryItemFactory() {
    throw new AssertionError();
  }

  public static InventoryItemBuilder builder() {
    return new InventoryItemBuilder();
  }

  public static InventoryItem of(@Nullable ItemStack itemStack) {
    return builder().item(itemStack).build();
  }

  public static InventoryItem of(@Nullable WrappedItemStack itemStack) {
    return builder().item(itemStack).build();
  }

  public static InventoryItem ofClickable(
      @Nullable ItemStack itemStack, @Nullable InventoryItemClickAction clickAction) {
    return builder().item(itemStack).setClickHandle(clickAction).build();
  }

  public static InventoryItem ofClickable(
      @Nullable WrappedItemStack itemStack, @Nullable InventoryItemClickAction clickAction) {
    return builder().item(itemStack).setClickHandle(clickAction).build();
  }

  public static class InventoryItemBuilder {
    private @Nullable Function<InventoryItemAccessor, @Nullable ItemStack> itemFactory;
    private @Nullable InventoryItemClickAction clickAction;

    protected InventoryItemBuilder() {}

    @CanIgnoreReturnValue
    public InventoryItemBuilder setClickHandle(@Nullable InventoryItemClickAction action) {
      this.clickAction = action;
      return this;
    }

    @CanIgnoreReturnValue
    public InventoryItemBuilder addClickHandle(@Nullable InventoryItemClickAction action) {
      this.clickAction = (clickAction != null ? clickAction.andThen(action) : action);
      return this;
    }

    @CanIgnoreReturnValue
    public InventoryItemBuilder cancel() {
      return addClickHandle(InventoryItemClickAction.CANCELLING);
    }

    @CanIgnoreReturnValue
    public InventoryItemBuilder item(
        @Nullable Function<InventoryItemAccessor, @Nullable ItemStack> itemFactory) {
      this.itemFactory = itemFactory;
      return this;
    }

    @CanIgnoreReturnValue
    public InventoryItemBuilder item(@Nullable ItemStackSupplier itemFactory) {
      this.itemFactory = (itemFactory != null ? (accessor) -> itemFactory.getItemStack() : null);
      return this;
    }

    @CanIgnoreReturnValue
    public InventoryItemBuilder item(@Nullable ItemStack itemStack) {
      this.itemFactory = (itemStack != null ? (accessor) -> itemStack : null);
      return this;
    }

    @CanIgnoreReturnValue
    public InventoryItemBuilder item(@Nullable WrappedItemStack itemStack) {
      this.itemFactory = (itemStack != null ? (accessor) -> itemStack.getItemStack() : null);
      return this;
    }

    @CheckReturnValue
    public InventoryItem build() {
      // copy pointers to avoid mutation of returning item on mutation of this builder
      @Nullable Function<InventoryItemAccessor, @Nullable ItemStack> factory = itemFactory;
      @Nullable InventoryItemClickAction clickAction = this.clickAction;
      return new InventoryItem() {
        @Override
        public @Nullable ItemStack get(@NonNull InventoryItemAccessor accessor) {
          return (factory != null ? factory.apply(accessor) : null);
        }

        @Override
        public void handleClick(@NonNull InventoryClickEvent event) {
          if (clickAction != null) clickAction.handleClick(event);
        }
      };
    }
  }

}
