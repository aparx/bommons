package io.github.aparx.bommons.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import io.github.aparx.bommons.core.utils.ConversionUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.NumberConversions;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.*;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-21 21:13
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class ItemStackBuilder implements ConfigurationSerializable {

  static {
    // TODO: potentially remove due to CommonsLibrary#<init> (?)
    ConfigurationSerialization.registerClass(ItemStackBuilder.class);
  }

  protected static final ItemFlag[] EMPTY_FLAGS = new ItemFlag[0];

  protected static final int DEFAULT_AMOUNT = 1;

  private final Map<Enchantment, Integer> enchants = new HashMap<>();

  private Material type;
  private @NonNegative int amount;
  private @Nullable String name;
  private ImmutableList<@Nullable String> lore = ImmutableList.of();
  private ItemFlag[] flags = EMPTY_FLAGS;

  private @Nullable StackMetaModifier modifier;

  public ItemStackBuilder() {
    this(Material.AIR);
  }

  public ItemStackBuilder(Material type) {
    this(type, DEFAULT_AMOUNT);
  }

  @SuppressWarnings("ConstantValue")
  public ItemStackBuilder(Material type, @NonNegative int amount) {
    Preconditions.checkNotNull(type, "Type must not be null");
    Preconditions.checkArgument(amount >= 0, "Amount must not be negative");
    this.type(type); // call method to ensure overridden preconditions
    this.amount = amount;
  }

  public ItemStackBuilder(ItemStackBuilder source) {
    this.type(source.getType()); // call method to ensure overridden preconditions
    this.name = source.getName();
    this.amount = source.getAmount();
    this.lore = source.getLore();
    this.flags = source.getFlags();
    this.modifier = (source.modifier != null ? source.modifier.copy() : null);
    this.enchants.putAll(source.enchants);
  }

  protected ItemStackBuilder(Map<?, @Nullable ?> args) {
    String type = Objects.toString(args.get("type"), null);
    Preconditions.checkNotNull(type, "Missing type");
    this.type = getSerializedType(type);
    this.amount(NumberConversions.toInt(args.get("amount")));
    this.name(Objects.toString(args.get("name"), null));
    if (args.containsKey("lore"))
      this.lore(ConversionUtil.objectToStringList(args.get("lore")));
    if (args.containsKey("flags"))
      this.flags(ConversionUtil
          .toEnumList(ItemFlag.class, ConversionUtil.objectToStringList(args.get("flags")))
          .toArray(ItemFlag[]::new));
    if (args.containsKey("enchants")) {
      Map<Enchantment, Integer> enchants = new HashMap<>();
      ConversionUtil.objectToStringObjectMap(args.get("enchants"), HashMap::new)
          .forEach((key, value) -> {
            if (key == null) return;
            @Nullable Enchantment byKey = Enchantment.getByKey(NamespacedKey.minecraft(key));
            if (byKey != null)
              enchants.put(byKey, NumberConversions.toInt(value));
          });
      this.enchants(enchants);
    }
    Object modifierObject = args.get("modifier");
    if (modifierObject instanceof StackMetaModifier)
      this.modifier = (StackMetaModifier) modifierObject;
  }

  public static Material getSerializedType(String typeString) {
    @Nullable Material material = Material.getMaterial(typeString);
    if (material == null)
      material = ConversionUtil.toEnum(Material.class, typeString);
    return material;
  }

  @Override
  public Map<String, Object> serialize() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("type", getType().name().toLowerCase().replace('_', ' '));
    map.put("amount", getAmount());
    @Nullable String name = getName();
    if (name != null)
      map.put("name", name);
    map.put("lore", getLore());
    if (ArrayUtils.isNotEmpty(flags))
      map.put("flags", ConversionUtil.toStringCollection(flags, ArrayList::new));
    if (!enchants.isEmpty()) {
      Map<String, Object> enchants = new HashMap<>(this.enchants.size());
      this.enchants.forEach((enchantment, level) -> {
        enchants.put(enchantment.getKey().getKey(), level);
      });
      map.put("enchants", enchants);
    }
    if (modifier != null)
      map.put("modifier", modifier);
    return map;
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder copyFrom(ItemStack itemStack) {
    this.type(itemStack.getType()).amount(itemStack.getAmount());
    @Nullable ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta != null && itemStack.hasItemMeta())
      return copyFrom(itemMeta);
    return this;
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder copyFrom(ItemMeta itemMeta) {
    if (itemMeta.hasDisplayName())
      this.name(itemMeta.getDisplayName());
    if (itemMeta.hasLore())
      this.lore(itemMeta.getLore());
    if (itemMeta.hasEnchants())
      this.enchants(itemMeta.getEnchants());
    this.flags = itemMeta.getItemFlags().toArray(ItemFlag[]::new);
    return this;
  }

  @CanIgnoreReturnValue
  public ItemStack applyOnto(ItemStack itemStack) {
    itemStack.setType(type);
    itemStack.setAmount(amount);
    @Nullable ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta != null && itemStack.hasItemMeta())
      applyOnto(itemMeta);
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  @CanIgnoreReturnValue
  public ItemMeta applyOnto(ItemMeta itemMeta) {
    itemMeta.setLore(lore.isEmpty() ? null : lore);
    itemMeta.setDisplayName(name);
    if (ArrayUtils.isNotEmpty(flags))
      itemMeta.addItemFlags(flags);
    // TODO add attributes
    enchants.forEach((enchantment, level) -> {
      itemMeta.addEnchant(enchantment, level, false);
    });
    return itemMeta;
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder type(Material type) {
    Preconditions.checkNotNull(type, "Type must not be null");
    this.type = type;
    return this;
  }

  public final Material getType() {
    return type;
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder amount(int amount) {
    Preconditions.checkArgument(amount >= 0, "Amount must not be negative");
    this.amount = amount;
    return this;
  }

  public final int getAmount() {
    return amount;
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder name(@Nullable String displayName) {
    this.name = displayName;
    return this;
  }

  public final @Nullable String getName() {
    return name;
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder lore(@Nullable List<@Nullable String> lore) {
    this.lore = (lore == null ? ImmutableList.of() : ImmutableList.copyOf(lore));
    return this;
  }

  @CanIgnoreReturnValue
  @SuppressWarnings("DataFlowIssue") // OK, ensured through ArrayUtils
  public ItemStackBuilder lore(@Nullable String @Nullable ... lines) {
    this.lore = (ArrayUtils.isEmpty(lines) ? ImmutableList.of() : ImmutableList.copyOf(lines));
    return this;
  }

  public final ImmutableList<@Nullable String> getLore() {
    return this.lore;
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder flags(ItemFlag @Nullable ... flags) {
    if (flags != null)
      Validate.noNullElements(flags, "Flag must not be null");
    this.flags = (flags != null ? ArrayUtils.clone(flags) : EMPTY_FLAGS);
    return this;
  }

  public final ItemFlag[] getFlags() {
    return ArrayUtils.clone(flags);
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder enchant(Enchantment enchantment, int level) {
    Preconditions.checkNotNull(enchantment, "Enchantment must not be null");
    enchants.put(enchantment, level);
    return this;
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder enchants(Map<Enchantment, Integer> enchantLevelMap) {
    Preconditions.checkNotNull(enchantLevelMap, "Map must not be null");
    enchants.putAll(enchantLevelMap);
    return this;
  }

  public int getEnchantmentLevel(Enchantment enchantment) {
    Integer levelFound = enchants.get(enchantment);
    return (levelFound != null ? levelFound : -1);
  }

  public boolean hasEnchantment(Enchantment enchantment) {
    return enchants.containsKey(enchantment);
  }

  public final Map<Enchantment, Integer> getEnchantments() {
    return new HashMap<>(enchants);
  }

  @CanIgnoreReturnValue
  public ItemStackBuilder modifier(@Nullable StackMetaModifier modifier) {
    this.modifier = modifier;
    return this;
  }

  public @Nullable StackMetaModifier getModifier() {
    return modifier;
  }

  @CheckReturnValue
  public ItemStack build() {
    return applyOnto(new ItemStack(Material.AIR));
  }

  @CheckReturnValue
  public WrappedItemStack wrap() {
    return new WrappedItemStack(build());
  }

  public ItemStackBuilder copy() {
    return new ItemStackBuilder(this);
  }

  @Override
  public String toString() {
    return "ItemStackBuilder{" +
        "enchants=" + enchants +
        ", type=" + type +
        ", amount=" + amount +
        ", name='" + name + '\'' +
        ", lore=" + lore +
        ", flags=" + Arrays.toString(flags) +
        '}';
  }
}

