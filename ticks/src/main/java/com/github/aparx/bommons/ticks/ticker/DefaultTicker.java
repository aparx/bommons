package com.github.aparx.bommons.ticks.ticker;

import com.google.common.base.Preconditions;
import com.github.aparx.bommons.ticks.TickDuration;
import com.github.aparx.bommons.ticks.TickTimeUnit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.function.LongUnaryOperator;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 01:01
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class DefaultTicker extends AbstractTicker {

  protected final transient Object lock = new Object();

  private final TickDuration defaultInterval;

  private volatile long elapsed;
  private @Nullable TickDuration elapsedDuration;

  public DefaultTicker() {
    this(TickDuration.ofNil());
  }

  public DefaultTicker(TickTimeUnit unit) {
    this(TickDuration.ofNil(unit));
  }

  public DefaultTicker(TickDuration defaultInterval) {
    Preconditions.checkNotNull(defaultInterval, "Default interval must not be null");
    this.defaultInterval = defaultInterval;
  }

  public TickDuration getDefaultInterval() {
    return defaultInterval;
  }

  @Override
  public void reset() {
    set(0);
  }

  @Override
  public long tick() {
    synchronized (lock) {
      long amount = ++this.elapsed;
      elapsedDuration = null;
      return amount;
    }
  }

  @Override
  public void set(long ticks) {
    synchronized (lock) {
      this.elapsed = ticks;
      elapsedDuration = null;
    }
  }

  @Override
  public long update(@NonNull LongUnaryOperator updater) {
    synchronized (lock) {
      long amount = updater.applyAsLong(elapsed);
      elapsedDuration = null;
      return amount;
    }
  }

  @Override
  public long getElapsed() {
    synchronized (lock) {
      return elapsed;
    }
  }

  @Override
  public long getElapsed(TickTimeUnit time) {
    return getElapsedDuration().convertAmount(time);
  }

  @Override
  public TickDuration getElapsedDuration() {
    if (elapsedDuration != null)
      return elapsedDuration;
    synchronized (lock) {
      if (elapsedDuration != null)
        return elapsedDuration;
      // we increment `defaultInterval` by one to have valid interval ticks
      // even if we have no interval (required since interval is factor)
      elapsedDuration = getElapsedDuration(defaultInterval.add(1));
      return elapsedDuration;
    }
  }

  @Override
  public TickDuration getElapsedDuration(TickDuration interval) {
    // TODO this isn't always accurate with bukkit tasks
    return TickDuration.of(TickTimeUnit.TICKS, getElapsed() * interval.toTicks());
  }
}
