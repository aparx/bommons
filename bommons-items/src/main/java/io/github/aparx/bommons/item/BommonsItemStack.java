package io.github.aparx.bommons.item;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CheckReturnValue;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;
import java.util.Objects;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-21 21:39
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
@SerializableAs("Bommons.ItemStack")
public class BommonsItemStack implements ConfigurationSerializable {

  static {
    // TODO: potentially remove due to CommonsLibrary#<init> (?)
    ConfigurationSerialization.registerClass(BommonsItemStack.class);
  }

  private final ItemStack itemStack;

  public BommonsItemStack(ItemStack itemStack) {
    Preconditions.checkNotNull(itemStack, "ItemStack must not be null");
    this.itemStack = itemStack;
  }

  protected BommonsItemStack(Map<?, @Nullable ?> args) {
    this.itemStack = new ItemStackBuilder(args).build();
  }

  @Override
  public Map<String, Object> serialize() {
    return toBuilder().serialize();
  }

  @CheckReturnValue
  public ItemStackBuilder toBuilder() {
    return new ItemStackBuilder().copyFrom(itemStack);
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    BommonsItemStack that = (BommonsItemStack) object;
    return Objects.equals(itemStack, that.itemStack);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemStack);
  }

  @Override
  public String toString() {
    return "CommonsItemStack{" +
        "itemStack=" + itemStack +
        '}';
  }
}
