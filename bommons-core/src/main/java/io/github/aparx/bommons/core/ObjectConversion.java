package io.github.aparx.bommons.core;

import org.bukkit.configuration.MemorySection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-21 21:44
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public final class ObjectConversion {

  /** Same as ArrayList's default capacity */
  private static final int DEFAULT_COLLECTION_CAPACITY = 10;

  private ObjectConversion() {
    throw new AssertionError();
  }

  // +------------------ Enum conversions ------------------+

  public static <E extends Enum<E>> E toEnum(Class<E> enumType, Object object) {
    if (enumType.isInstance(object))
      return enumType.cast(object);
    String stringValue = object.toString().trim();
    stringValue = stringValue.toUpperCase();
    stringValue = stringValue.replace(' ', '_');
    return Enum.valueOf(enumType, stringValue);
  }

  public static <E extends Enum<E>, R extends Collection<E>>
  R toEnumCollection(Class<E> enumType, Collection<@Nullable ?> objects,
                     IntFunction<R> factory) {
    R collection = factory.apply(objects.size());
    objects.forEach((object) -> {
      if (object != null)
        collection.add(toEnum(enumType, object));
    });
    return collection;
  }

  public static <E extends Enum<E>>
  List<E> toEnumList(Class<E> enumType, Collection<@Nullable ?> objects) {
    return toEnumCollection(enumType, objects, ArrayList::new);
  }

  // +------------------ Collection conversions ------------------+

  public static <R extends Collection<@Nullable String>>
  R objectToStringCollection(Object object, IntFunction<R> factory) {
    if (object instanceof MemorySection)
      return toStringCollection((MemorySection) object, factory);
    if (object.getClass().isArray())
      return toStringCollection((Object[]) object, factory);
    if (object instanceof Iterator)
      return toStringCollection((Iterator<?>) object, factory);
    if (!(object instanceof Iterable))
      throw new IllegalArgumentException("Object is not collection-like");
    R collection = (object instanceof Collection
        ? factory.apply(((Collection<?>) object).size())
        : factory.apply(DEFAULT_COLLECTION_CAPACITY));
    fillStringCollection(((Iterable<?>) object).iterator(), collection);
    return collection;
  }

  public static List<@Nullable String> objectToStringList(Object object) {
    return objectToStringCollection(object, ArrayList::new);
  }

  public static <R extends Collection<@Nullable String>>
  R toStringCollection(MemorySection section, IntFunction<R> factory) {
    return objectToStringCollection(section.getKeys(false), factory);
  }

  public static <R extends Collection<@Nullable String>>
  R toStringCollection(@Nullable Object[] array, IntFunction<R> factory) {
    return objectToStringCollection(Arrays.asList(array), factory);
  }

  public static <R extends Collection<@Nullable String>>
  R toStringCollection(Iterable<@Nullable ?> iterable, IntFunction<R> factory) {
    return objectToStringCollection(iterable, factory);
  }

  public static <R extends Collection<@Nullable String>>
  R toStringCollection(Iterator<@Nullable ?> iterator, IntFunction<R> factory) {
    R collection = factory.apply(DEFAULT_COLLECTION_CAPACITY);
    fillStringCollection(iterator, collection);
    return collection;
  }

  private static void fillStringCollection(
      Iterator<@Nullable ?> iterator, Collection<@Nullable String> collection) {
    while (iterator.hasNext())
      collection.add(Objects.toString(iterator.next(), null));
  }

  // +------------------ Map conversions ------------------+

  // <?, ?> -> <String, ?>

  public static <R extends Map<@Nullable String, @Nullable Object>>
  R objectToStringObjectMap(Object object, IntFunction<R> factory) {
    if (object instanceof MemorySection)
      return toStringObjectMap((MemorySection) object, factory);
    if (!(object instanceof Map))
      throw new IllegalArgumentException("Object is not map-like");
    return toStringObjectMap((Map<?, ?>) object, factory);
  }

  public static <R extends Map<@Nullable String, @Nullable Object>>
  R toStringObjectMap(Map<@Nullable ?, @Nullable ?> map, IntFunction<R> factory) {
    R output = factory.apply(map.size());
    map.forEach((key, value) -> output.put(Objects.toString(key, null), value));
    return output;
  }

  public static <R extends Map<@Nullable String, @Nullable Object>>
  R toStringObjectMap(MemorySection section, IntFunction<R> factory) {
    return toStringObjectMap(section.getValues(false), factory);
  }

  // <?, ?> -> <String, R>

  @SuppressWarnings("unchecked") // OK, only for lookup purposes
  public static <@Nullable VOut, R extends Map<@Nullable String, VOut>>
  R objectToStringSpecificMap(Object object, IntFunction<R> factory,
                              Function<Object, VOut> valueMapper) {
    if (object instanceof MemorySection)
      return toStringSpecificMap((MemorySection) object, factory, valueMapper);
    if (!(object instanceof Map))
      throw new IllegalArgumentException("Object is not map-like");
    return toStringSpecificMap((Map<?, Object>) object, factory, valueMapper);
  }

  public static <@Nullable VOut, R extends Map<@Nullable String, VOut>>
  R toStringSpecificMap(MemorySection section, IntFunction<R> factory,
                        Function<Object, VOut> valueMapper) {
    return toStringSpecificMap(section.getValues(false), factory, valueMapper);
  }

  public static <VIn, @Nullable VOut, R extends Map<@Nullable String, VOut>>
  R toStringSpecificMap(Map<@Nullable ?, VIn> map, IntFunction<R> factory,
                        Function<VIn, VOut> valueMapper) {
    R output = factory.apply(map.size());
    map.forEach((key, value) -> output.put(Objects.toString(key, null), valueMapper.apply(value)));
    return output;
  }

}
