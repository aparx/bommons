import com.sun.source.tree.AssertTree;
import io.github.aparx.bommons.core.ticks.TickDuration;
import io.github.aparx.bommons.core.ticks.TickTimeUnit;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 00:16
 * @since 1.0
 */
public class TestTickDuration {

  @Test
  public void testNilCache() {
    for (TickTimeUnit unit : TickTimeUnit.values()) {
      Assert.assertSame(TickDuration.of(unit, 0), TickDuration.of(unit, 0));
      Assert.assertNotSame(TickDuration.of(unit, 1), TickDuration.of(unit, 1));
      Assert.assertNotSame(TickDuration.of(unit, -1), TickDuration.of(unit, -1));
    }
  }

  @Test
  public void testArithmeticOperations() {
    for (TickTimeUnit unit : TickTimeUnit.values()) {
      Assert.assertEquals(2, TickDuration.of(unit, 1).add(1).getAmount());
      Assert.assertEquals(2, TickDuration.of(unit, 3).subtract(1).getAmount());
      Assert.assertEquals(2, TickDuration.of(unit, 1).multiply(2).getAmount());
      Assert.assertEquals(2, TickDuration.of(unit, 4).divide(2).getAmount());
    }
  }

  /**
   * Tests {@code convertAmount}, {@code convert} and several conversions (such as {@code toTicks},
   * {@code toSeconds}, etc.) implicitly.
   */
  @Test
  public void testConversions() {
    Assert.assertEquals(40, TickDuration.of(TickTimeUnit.SECONDS, 2).toTicks());
    Assert.assertEquals(2, TickDuration.of(TickTimeUnit.SECONDS, 2).toSeconds());
    Assert.assertEquals(1, TickDuration.of(TickTimeUnit.SECONDS, 60).toMinutes());
    Assert.assertEquals(0, TickDuration.of(TickTimeUnit.SECONDS, 60).toHours());

    Assert.assertEquals(60 * 20, TickDuration.of(TickTimeUnit.MINUTES, 1).toTicks());
    Assert.assertEquals(1, TickDuration.of(TickTimeUnit.MINUTES, 1).toMinutes());
    Assert.assertEquals(1, TickDuration.of(TickTimeUnit.MINUTES, 60).toHours());
    Assert.assertEquals(0, TickDuration.of(TickTimeUnit.MINUTES, 60).toDays());

    Assert.assertEquals(60 * 60 * 20, TickDuration.of(TickTimeUnit.HOURS, 1).toTicks());
    Assert.assertEquals(1, TickDuration.of(TickTimeUnit.HOURS, 1).toHours());
    Assert.assertEquals(60, TickDuration.of(TickTimeUnit.HOURS, 1).toMinutes());
    Assert.assertEquals(2, TickDuration.of(TickTimeUnit.HOURS, 60).toDays());
    Assert.assertEquals(0, TickDuration.of(TickTimeUnit.HOURS, 60).toWeeks());

    Assert.assertEquals(24 * 60 * 60 * 20, TickDuration.of(TickTimeUnit.DAYS, 1).toTicks());
    Assert.assertEquals(1, TickDuration.of(TickTimeUnit.DAYS, 1).toDays());
    Assert.assertEquals(24, TickDuration.of(TickTimeUnit.DAYS, 1).toHours());
    Assert.assertEquals(8, TickDuration.of(TickTimeUnit.DAYS, 60).toWeeks());
    Assert.assertEquals(2, TickDuration.of(TickTimeUnit.DAYS, 60).toMonths());

    Assert.assertEquals(72, TickDuration.of(TickTimeUnit.DAYS, 3).toHours());
  }

  @Test
  public void testEquality() {
    Assert.assertEquals(TickDuration.one(TickTimeUnit.MINUTES),
        TickDuration.of(TickTimeUnit.SECONDS, 60));
    Assert.assertEquals(TickDuration.of(TickTimeUnit.MINUTES, 2),
        TickDuration.of(TickTimeUnit.SECONDS, 120));
    Assert.assertNotEquals(TickDuration.of(TickTimeUnit.MINUTES, 2),
        TickDuration.of(TickTimeUnit.SECONDS, 121));
  }

}
