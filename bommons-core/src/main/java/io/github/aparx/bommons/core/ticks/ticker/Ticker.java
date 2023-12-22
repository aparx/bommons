package io.github.aparx.bommons.core.ticks.ticker;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.aparx.bommons.core.ticks.TickDuration;
import io.github.aparx.bommons.core.ticks.TickTimeUnit;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.LongUnaryOperator;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 00:40
 * @since 1.0
 */
public interface Ticker {

  void reset();

  @CanIgnoreReturnValue
  long tick();

  void set(long ticks);

  @CanIgnoreReturnValue
  long update(@NonNull LongUnaryOperator updater);

  long getElapsed();

  long getElapsed(TickTimeUnit time);

  TickDuration getElapsedDuration();

  /**
   * Returns the elapsed duration in {@code interval}'s unit, for how the ticker has elapsed so
   * far, as if {@code interval} was the frequency with which this ticker has been incremented.
   *
   * @param interval the frequency to convert this elapsed time to
   * @return the elapsed amount of ticks converted to {@code interval}'s unit, as if {@code
   * interval} was the frequency of all ticker increments in this ticker
   */
  TickDuration getElapsedDuration(@NonNull TickDuration interval);

  boolean hasElapsed(long amount);

  boolean hasElapsed(@NonNull TickTimeUnit unit);

  boolean hasElapsed(@NonNull TickDuration duration);

  default boolean isCycling(long amount) {
    long elapsed = getElapsed();
    return amount == 0 || elapsed >= amount && elapsed % amount == 0;
  }

  default boolean isCycling(@NonNull TickTimeUnit unit) {
    long elapsedTicks = getElapsedDuration().toTicks();
    return elapsedTicks >= unit.getTicks() && elapsedTicks % unit.getTicks() == 0;
  }

  default boolean isCycling(@NonNull TickDuration duration) {
    long elapsedTicks = getElapsedDuration().toTicks();
    long ticks = duration.toTicks();
    return ticks == 0 || elapsedTicks >= ticks && elapsedTicks % ticks == 0;
  }

}
