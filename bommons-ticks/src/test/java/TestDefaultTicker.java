import io.github.aparx.bommons.ticks.TickDuration;
import io.github.aparx.bommons.ticks.TickTimeUnit;
import io.github.aparx.bommons.ticks.ticker.DefaultTicker;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-22 01:29
 * @since 1.0
 */
public class TestDefaultTicker {

  @Test
  public void testGetDuration() {
    DefaultTicker ticker = new DefaultTicker(TickTimeUnit.SECONDS);
    ticker.set(60);
    Assert.assertEquals(TickDuration.one(TickTimeUnit.MINUTES), ticker.getElapsedDuration());
    Assert.assertNotEquals(TickDuration.nil(TickTimeUnit.MINUTES), ticker.getElapsedDuration());
    Assert.assertEquals(TickDuration.nil(TickTimeUnit.HOURS), ticker.getElapsedDuration());
  }

  @Test
  public void testGetElapsed() {
    DefaultTicker ticker = new DefaultTicker(TickTimeUnit.SECONDS);
    ticker.set(60);
    Assert.assertEquals(60, ticker.getElapsed());
    Assert.assertEquals(20 * 60, ticker.getElapsed(TickTimeUnit.TICKS));
    Assert.assertEquals(1, ticker.getElapsed(TickTimeUnit.MINUTES));
    Assert.assertEquals(0, ticker.getElapsed(TickTimeUnit.HOURS));
  }

  @Test
  public void testIsCycling() {
    DefaultTicker ticker = new DefaultTicker(TickTimeUnit.SECONDS);
    ticker.set(120);
    Assert.assertTrue(ticker.isCycling(0));
    Assert.assertTrue(ticker.isCycling(60));
    Assert.assertTrue(ticker.isCycling(120));
    Assert.assertFalse(ticker.isCycling(180));
    Assert.assertFalse(ticker.isCycling(59));

    Assert.assertTrue(ticker.isCycling(TickTimeUnit.SECONDS));
    Assert.assertTrue(ticker.isCycling(TickDuration.one(TickTimeUnit.SECONDS)));
    Assert.assertTrue(ticker.isCycling(TickDuration.nil(TickTimeUnit.SECONDS)));
    Assert.assertTrue(ticker.isCycling(TickDuration.of(TickTimeUnit.SECONDS, 2)));
    Assert.assertTrue(ticker.isCycling(TickDuration.of(TickTimeUnit.MINUTES, 1)));
    Assert.assertTrue(ticker.isCycling(TickDuration.of(TickTimeUnit.MINUTES, 2)));
    Assert.assertFalse(ticker.isCycling(TickDuration.of(TickTimeUnit.MINUTES, 3)));
  }

}
