package io.github.aparx.bommons.core.ticks;

import com.google.common.base.Preconditions;
import org.checkerframework.checker.index.qual.NonNegative;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-21 23:52
 * @since 1.0
 */
public enum TickTimeUnit {

  TICKS(1),
  SECONDS(20),
  MINUTES(60 * SECONDS.ticks),
  HOURS(60 * MINUTES.ticks),
  DAYS(24 * HOURS.ticks),
  WEEKS(7 * DAYS.ticks),
  MONTHS(4 * WEEKS.ticks),
  YEARS(12 * MONTHS.ticks);

  private final long ticks;

  TickTimeUnit(@NonNegative long ticks) {
    Preconditions.checkArgument(ticks >= 1);
    this.ticks = ticks;
  }

  public final long getTicks() {
    return ticks;
  }

}
