package io.github.aparx.bommons.item.modifiers;

import io.github.aparx.bommons.item.ItemStackBuilder;
import io.github.aparx.bommons.item.StackMetaModifier;
import org.bukkit.Color;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 02:18
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class ArmorMetaModifier extends StackMetaModifier {

  private final @Nullable Color color;

  public ArmorMetaModifier(@Nullable Color color) {
    this.color = color;
  }

  public @Nullable Color getColor() {
    return color;
  }

  @Override
  public Map<String, Object> serialize() {
    if (color != null)
      return Map.of("color", color);
    return Map.of();
  }

  @Override
  public ItemMeta modify(ItemStackBuilder builder, ItemMeta meta) {
    if (meta instanceof LeatherArmorMeta)
      ((LeatherArmorMeta) meta).setColor(color);
    return meta;
  }

  @Override
  public ArmorMetaModifier copy() {
    return new ArmorMetaModifier(getColor());
  }
}
