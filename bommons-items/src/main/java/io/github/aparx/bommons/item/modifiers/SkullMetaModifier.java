package io.github.aparx.bommons.item.modifiers;

import io.github.aparx.bommons.item.ItemStackBuilder;
import io.github.aparx.bommons.item.StackMetaModifier;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;
import java.util.UUID;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 02:18
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
@SerializableAs("Bommons.SkullMetaModifier")
public class SkullMetaModifier extends StackMetaModifier {

  private final @Nullable OfflinePlayer owningPlayer;

  public SkullMetaModifier(@Nullable UUID owner) {
    this(owner != null ? Bukkit.getOfflinePlayer(owner) : null);
  }

  @SuppressWarnings("deprecation")
  public SkullMetaModifier(@Nullable String owner) {
    this(owner != null ? Bukkit.getOfflinePlayer(owner) : null);
  }

  public SkullMetaModifier(@Nullable OfflinePlayer owningPlayer) {
    this.owningPlayer = owningPlayer;
  }

  public @Nullable OfflinePlayer getOwningPlayer() {
    return owningPlayer;
  }

  @Override
  public Map<String, Object> serialize() {
    if (owningPlayer != null)
      return Map.of("owner", owningPlayer.getUniqueId());
    return Map.of();
  }

  @Override
  public ItemMeta modify(ItemStackBuilder builder, ItemMeta meta) {
    if (meta instanceof SkullMeta)
      ((SkullMeta) meta).setOwningPlayer(owningPlayer);
    return meta;
  }

  @Override
  public SkullMetaModifier copy() {
    return new SkullMetaModifier(getOwningPlayer());
  }
}
