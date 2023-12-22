package io.github.aparx.bommons.core.ticks.ticker;


import io.github.aparx.bommons.core.ticks.TickDuration;
import io.github.aparx.bommons.core.ticks.TickTimeUnit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 00:53
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public abstract class AbstractTicker implements Ticker {

  @Override
  public boolean hasElapsed(long amount) {
    return getElapsed() >= amount;
  }

  @Override
  public boolean hasElapsed(TickTimeUnit unit) {
    return getElapsedDuration().toTicks() >= unit.getTicks();
  }

  @Override
  public boolean hasElapsed(TickDuration duration) {
    return getElapsedDuration().toTicks() >= duration.toTicks();
  }

}
