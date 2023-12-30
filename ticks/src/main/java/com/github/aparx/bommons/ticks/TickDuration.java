package com.github.aparx.bommons.ticks;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CheckReturnValue;
import com.github.aparx.bommons.core.ObjectConversion;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.util.NumberConversions;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-21 23:54
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public final class TickDuration implements ConfigurationSerializable {

  private static final EnumMap<TickTimeUnit, TickDuration> frequentDurationCache =
      new EnumMap<>(TickTimeUnit.class);

  static {
    ConfigurationSerialization.registerClass(TickDuration.class);
  }

  /** Cached duration that represents this duration in ticks */
  private final transient TickDuration ticks;

  private final TickTimeUnit unit;
  private final long amount;

  private TickDuration(TickTimeUnit unit, long amount) {
    Preconditions.checkNotNull(unit, "Unit must not be null");
    this.unit = unit;
    this.amount = amount;
    this.ticks = (unit != TickTimeUnit.TICKS ? convert(TickTimeUnit.TICKS) : this);
  }

  public static TickDuration of(TickTimeUnit unit, long amount) {
    if (amount != 0)
      return new TickDuration(unit, amount);
    // caching optimization for `amount` of zero
    // (chances are high for frequent usage!)
    if (frequentDurationCache.containsKey(unit))
      return frequentDurationCache.get(unit);
    TickDuration tickDuration = new TickDuration(unit, amount);
    frequentDurationCache.put(unit, tickDuration);
    return tickDuration;
  }

  public static TickDuration deserialize(Map<?, @Nullable ?> args) {
    String unitString = Objects.toString(args.get("unit"), null);
    Preconditions.checkNotNull(unitString, "Unit must not be null");
    return of(ObjectConversion.toEnum(TickTimeUnit.class, unitString),
        NumberConversions.toLong(args.get("amount")));
  }

  @Override
  public Map<String, Object> serialize() {
    return Map.of("unit", unit.name().toLowerCase(), "amount", amount);
  }

  public static TickDuration ofNil() {
    return of(TickTimeUnit.TICKS, 0);
  }

  public static TickDuration ofNil(TickTimeUnit unit) {
    return of(unit, 0);
  }

  public static TickDuration ofOne() {
    return of(TickTimeUnit.TICKS, 1);
  }

  public static TickDuration ofOne(TickTimeUnit unit) {
    return of(unit, 1);
  }

  public TickTimeUnit getUnit() {
    return unit;
  }

  public long getAmount() {
    return amount;
  }

  // +------------------ Arithmetic ------------------+

  @CheckReturnValue
  public TickDuration add(long value) {
    return of(unit, this.amount + value);
  }

  @CheckReturnValue
  public TickDuration subtract(long value) {
    return of(unit, this.amount - value);
  }

  @CheckReturnValue
  public TickDuration multiply(long factor) {
    return of(unit, this.amount * factor);
  }

  @CheckReturnValue
  public TickDuration divide(long divisor) {
    return of(unit, this.amount / divisor);
  }

  // +------------------ Conversions ------------------+

  public TickDuration convert(TickTimeUnit targetUnit) {
    return of(targetUnit, convertAmount(targetUnit));
  }

  public long convertAmount(TickTimeUnit targetUnit) {
    Preconditions.checkNotNull(targetUnit, "Target unit must not be null");
    if (targetUnit == this.unit) return amount;
    if (targetUnit == TickTimeUnit.TICKS)
      return amount * unit.getTicks();
    long targetAsTicks = targetUnit.getTicks();
    long thisAsTicks = this.unit.getTicks();
    long tickDiff = targetAsTicks - thisAsTicks;
    return (tickDiff == 0 ? amount : (tickDiff > 0
        ? (amount * thisAsTicks) / targetAsTicks
        : amount * (thisAsTicks / targetAsTicks)));
  }

  public long toTicks() {
    return ticks.getAmount();
  }

  public TickDuration asTicks() {
    return ticks;
  }

  public long toSeconds() {
    return convertAmount(TickTimeUnit.SECONDS);
  }

  public TickDuration asSeconds() {
    return convert(TickTimeUnit.SECONDS);
  }

  public long toMinutes() {
    return convertAmount(TickTimeUnit.MINUTES);
  }

  public TickDuration asMinutes() {
    return convert(TickTimeUnit.MINUTES);
  }

  public long toHours() {
    return convertAmount(TickTimeUnit.HOURS);
  }

  public TickDuration asHours() {
    return convert(TickTimeUnit.HOURS);
  }

  public long toDays() {
    return convertAmount(TickTimeUnit.DAYS);
  }

  public TickDuration asDays() {
    return convert(TickTimeUnit.DAYS);
  }

  public long toWeeks() {
    return convertAmount(TickTimeUnit.WEEKS);
  }

  public TickDuration asWeeks() {
    return convert(TickTimeUnit.WEEKS);
  }

  public long toMonths() {
    return convertAmount(TickTimeUnit.MONTHS);
  }

  public TickDuration asMonths() {
    return convert(TickTimeUnit.MONTHS);
  }

  public long toYears() {
    return convertAmount(TickTimeUnit.YEARS);
  }

  public TickDuration asYears() {
    return convert(TickTimeUnit.YEARS);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    TickDuration that = (TickDuration) object;
    return (amount == that.amount && unit == that.unit)
        || ticks.amount == that.ticks.amount
        || convertAmount(that.unit) == that.convertAmount(unit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unit, amount);
  }

  @Override
  public String toString() {
    return "TickDuration{" +
        "unit=" + unit +
        ", amount=" + amount +
        '}';
  }

}
