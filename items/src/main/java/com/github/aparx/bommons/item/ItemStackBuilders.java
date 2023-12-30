package com.github.aparx.bommons.item;

import com.github.aparx.bommons.item.modifiers.ArmorMetaModifier;
import com.github.aparx.bommons.item.modifiers.SkullMetaModifier;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.UUID;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 02:06
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public final class ItemStackBuilders {

  private ItemStackBuilders() {
    throw new AssertionError();
  }

  public static ItemStackBuilder create() {
    return new ItemStackBuilder();
  }

  public static ItemStackBuilder create(Material type) {
    return new ItemStackBuilder(type);
  }

  public static ItemStackBuilder create(Material type, int amount) {
    return new ItemStackBuilder(type, amount);
  }

  public static ItemStack build(Material type) {
    return create(type).build();
  }

  public static ItemStack build(Material type, int amount) {
    return create(type, amount).build();
  }

  public static ItemStackBuilder createSkull(@Nullable OfflinePlayer owner) {
    return create().modifier(new SkullMetaModifier(owner));
  }

  public static ItemStackBuilder createSkull(@Nullable UUID owner) {
    return create().modifier(new SkullMetaModifier(owner));
  }

  public static ItemStackBuilder createSkull(@Nullable String owner) {
    return create().modifier(new SkullMetaModifier(owner));
  }

  public static ItemStackBuilder createArmor(@NonNull Color color) {
    return create().modifier(new ArmorMetaModifier(color));
  }

}
