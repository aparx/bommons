package io.github.aparx.bommons.item;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 02:18
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public abstract class StackMetaModifier implements ConfigurationSerializable {

  public abstract ItemMeta modify(ItemStackBuilder builder, ItemMeta meta);

  public abstract StackMetaModifier copy();

}
