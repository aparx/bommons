package io.github.aparx.bommons.plugin;

import io.github.aparx.bommons.item.WrappedItemStack;
import io.github.aparx.bommons.item.ItemStackBuilder;
import io.github.aparx.bommons.item.modifiers.ArmorMetaModifier;
import io.github.aparx.bommons.item.modifiers.SkullMetaModifier;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-21 23:13
 * @since 1.0
 */
public final class CommonsLibrary {

  public static final CommonsLibrary instance = new CommonsLibrary();

  private CommonsLibrary() {
    ConfigurationSerialization.registerClass(WrappedItemStack.class);
    ConfigurationSerialization.registerClass(ItemStackBuilder.class);
    ConfigurationSerialization.registerClass(ArmorMetaModifier.class);
    ConfigurationSerialization.registerClass(SkullMetaModifier.class);
  }

  public static CommonsLibrary getInstance() {
    return instance;
  }

  public void load() {
    // TODO
  }
}
