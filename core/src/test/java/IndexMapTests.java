import com.github.aparx.bommons.core.IndexMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-18 10:55
 * @since 1.0
 */
public class IndexMapTests {

  @Test
  public void size() {
    IndexMap<String> map = new IndexMap<>();
    map.putAll(new String[]{"a", "b", "c", "d", "e"});
    Assert.assertEquals(5, map.size());
    map.remove(1);
    Assert.assertEquals(4, map.size());
    map.remove(3);
    Assert.assertEquals(3, map.size());
    map.clear();
    Assert.assertEquals(0, map.size());
    Assert.assertEquals(IndexMap.DEFAULT_INITIAL_CAPACITY, map.capacity());
  }

  @Test
  public void put() {
    IndexMap<String> map = new IndexMap<>();
    map.put(IndexMap.Entry.of(0, "hello"));
    map.put(IndexMap.Entry.of(1, "world"));
    Assert.assertEquals("hello", map.get(0));
    Assert.assertEquals("world", map.get(1));
    Assert.assertEquals("hello", map.put(0, "hello"));
    Assert.assertEquals("world", map.put(1, "world"));
    Assert.assertEquals("hello", map.get(0));
    Assert.assertEquals("world", map.get(1));
    Assert.assertEquals("hello", map.put(0, "hello"));
    Assert.assertEquals("world", map.remove(1));
    Assert.assertNull(map.put(2, "world"));
    Assert.assertEquals("hello", map.get(0));
    Assert.assertEquals("world", map.get(2));
    Assert.assertNull(map.get(1));
  }

  @Test
  public void putAll() {
    IndexMap<String> map = new IndexMap<>();
    map.put(0, "hello");
    map.put(3, "world");
    map.putAll(new String[]{"a", "b", "c"});
    Assert.assertEquals("a", map.get(0));
    Assert.assertEquals("b", map.get(1));
    Assert.assertEquals("c", map.get(2));
    Assert.assertEquals("world", map.get(3));

    map.putAll(Map.of(0, "hello", 3, "there"));
    Assert.assertEquals("hello", map.get(0));
    Assert.assertEquals("b", map.get(1));
    Assert.assertEquals("c", map.get(2));
    Assert.assertEquals("there", map.get(3));
  }

  @Test
  public void contains() {
    IndexMap<String> map = new IndexMap<>();
    map.put(0, "a");
    map.put(1, "b");
    map.put(2, "c");
    map.put(3, null);
    map.put(5, null);
    Assert.assertTrue(map.contains(0, "a"));
    Assert.assertFalse(map.contains(0, "b"));
    Assert.assertTrue(map.contains(1, "b"));
    Assert.assertFalse(map.contains(1, "c"));
    Assert.assertTrue(map.contains(2, "c"));
    Assert.assertFalse(map.contains(3, "d"));
    Assert.assertTrue(map.contains(3, null));
    Assert.assertFalse(map.contains(4, null));
    Assert.assertTrue(map.contains(5, null));
    Assert.assertFalse(map.contains(6, "a"));
  }

  @Test
  public void containsKey() {
    IndexMap<String> map = new IndexMap<>();
    map.put(0, "a");
    map.put(1, "b");
    map.put(2, "c");
    map.put(3, null);
    map.put(5, null);
    Assert.assertTrue(map.containsKey(0));
    Assert.assertTrue(map.containsKey(1));
    Assert.assertTrue(map.containsKey(2));
    Assert.assertTrue(map.containsKey(3));
    Assert.assertFalse(map.containsKey(4));
    Assert.assertTrue(map.containsKey(5));
    Assert.assertFalse(map.containsKey(6));
    Assert.assertFalse(map.containsKey(7));
  }

  @Test
  public void containsValue() {
    IndexMap<String> map = new IndexMap<>();
    map.put(0, "a");
    map.put(1, "b");
    map.put(2, "c");
    map.put(3, null);
    map.put(5, null);
    Assert.assertTrue(map.containsValue("a"));
    Assert.assertTrue(map.containsValue("b"));
    Assert.assertTrue(map.containsValue("c"));
    Assert.assertTrue(map.containsValue(null));
    Assert.assertFalse(map.containsValue("d"));
  }

  @Test
  public void indexOf() {
    IndexMap<String> map = new IndexMap<>();
    map.put(0, "a");
    map.put(1, "b");
    map.put(2, "c");
    map.put(3, null);
    map.put(5, null);
    Assert.assertEquals(0, map.indexOf("a"));
    Assert.assertEquals(1, map.indexOf("b"));
    Assert.assertEquals(2, map.indexOf("c"));
    Assert.assertEquals(3, map.indexOf(null));
    Assert.assertEquals(-1, map.indexOf("d"));
  }

  @Test
  public void lastIndexOf() {
    IndexMap<String> map = new IndexMap<>();
    map.put(0, "a");
    map.put(1, "a");
    map.put(2, "b");
    map.put(3, "b");
    map.put(4, "c");
    map.put(5, "c");
    map.put(6, null);
    map.put(7, null);
    Assert.assertEquals(1, map.lastIndexOf("a"));
    Assert.assertEquals(3, map.lastIndexOf("b"));
    Assert.assertEquals(5, map.lastIndexOf("c"));
    Assert.assertEquals(7, map.lastIndexOf(null));
    Assert.assertEquals(-1, map.lastIndexOf("d"));
  }


}