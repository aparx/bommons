package io.github.aparx.bommons.item;

import io.github.aparx.bommons.item.modifiers.ArmorMetaModifier;
import io.github.aparx.bommons.item.modifiers.SkullMetaModifier;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 02:06
 * @since 1.0
 */
public final class BommonsItemStacks {

  private BommonsItemStacks() {
    throw new AssertionError();
  }

  public static ItemStackBuilder createBuilder() {
    return new ItemStackBuilder();
  }

  public static ItemStackBuilder createSkull(@Nullable OfflinePlayer owner) {
    return createBuilder().modifier(new SkullMetaModifier(owner));
  }

  public static ItemStackBuilder createSkull(@Nullable UUID owner) {
    return createBuilder().modifier(new SkullMetaModifier(owner));
  }

  public static ItemStackBuilder createSkull(@Nullable String owner) {
    return createBuilder().modifier(new SkullMetaModifier(owner));
  }

  public static ItemStackBuilder createArmor(@NonNull Color color) {
    return createBuilder().modifier(new ArmorMetaModifier(color));
  }

}
