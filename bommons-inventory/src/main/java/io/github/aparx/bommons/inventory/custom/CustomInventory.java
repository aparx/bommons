package io.github.aparx.bommons.inventory.custom;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.aparx.bommons.core.utils.WeakHashSet;
import io.github.aparx.bommons.inventory.InventoryDimensions;
import io.github.aparx.bommons.inventory.item.InventoryItem;
import io.github.aparx.bommons.inventory.item.InventoryItemAccessor;
import io.github.aparx.bommons.ticks.TickDuration;
import io.github.aparx.bommons.ticks.TickTimeUnit;
import io.github.aparx.bommons.ticks.ticker.DefaultTicker;
import io.github.aparx.bommons.ticks.ticker.Ticker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author aparx (Vinzent Z.)
 * @version 2023-12-23 15:25
 * @since 1.0
 */
// a CustomInventory is used to display, render and update inventories (menus)
@DefaultQualifier(NonNull.class)
public class CustomInventory implements InventoryItemAccessor {

  private final transient Object lock = new Object();

  private final Plugin plugin;
  private final TickDuration updateInterval;
  private final WeakHashSet<Player> viewers = new WeakHashSet<>();
  private final Ticker updateTicker;
  private final String title;

  /** Current update task running for all viewers */
  protected @Nullable BukkitTask task;
  protected @Nullable InventoryContentView content;
  protected @Nullable Inventory inventory;

  protected final CustomInventoryListener listener = new CustomInventoryListener(this);

  public CustomInventory(Plugin plugin, String title) {
    this(plugin, TickDuration.nil(), title);
  }

  public CustomInventory(Plugin plugin, TickDuration updateInterval, String title) {
    Preconditions.checkNotNull(plugin, "Plugin must not be null");
    Preconditions.checkNotNull(updateInterval, "Interval must not be null");
    this.plugin = plugin;
    this.updateInterval = updateInterval;
    this.updateTicker = new DefaultTicker(updateInterval);
    this.title = title;
  }

  public void updateContent(InventoryContentView content) {
    Preconditions.checkNotNull(content, "Content must not be null");
    @Nullable InventoryDimensions currentInventoryDimensions = (
        this.content != null ? this.content.getDimensions() : null);
    this.content = content;
    if (!Objects.equals(currentInventoryDimensions, content.getDimensions()))
      createInventory();
  }

  @CanIgnoreReturnValue
  public boolean show(Player viewer) {
    Preconditions.checkNotNull(viewer, "Viewer must not be null");
    if (inventory == null) createInventory();
    synchronized (lock) {
      if (!viewers.add(viewer)) {
        viewer.openInventory(inventory);
        return false;
      }
      renderInventory(false);
      start(); // ensure render update-task start
      viewer.openInventory(inventory);
      return true;
    }
  }

  @CanIgnoreReturnValue
  public boolean close(Player viewer) {
    if (viewer.getOpenInventory().getTopInventory().equals(inventory))
      viewer.closeInventory();
    synchronized (lock) {
      if (!viewers.remove(viewer))
        return false;
      revalidateTask();
      return true;
    }
  }

  public boolean isViewer(Player player) {
    synchronized (lock) {
      return viewers.contains(player);
    }
  }

  public void updateInventory() {
    if (!renderInventory(updateTicker.getElapsed() > 1))
      updateTicker.tick(); // increase ticker
    else
      updateTicker.reset();
  }

  /** Renders this inventory and returns true if the update task is stopped */
  @CanIgnoreReturnValue
  public boolean renderInventory(boolean checkForViewers) {
    if (revalidateTask()) return true;
    if (content == null || inventory == null) return false;
    content.getArea().forEach((position) -> {
      @Nullable InventoryItem item = content.get(this, position);
      inventory.setItem(position.getIndex(), (item != null ? item.get(this) : null));
    });
    if (!checkForViewers)
      return false;
    List<Player> removeViewers = new ArrayList<>(0);
    viewers.forEach((viewer) -> {
      Inventory topInventory = viewer.getOpenInventory().getTopInventory();
      if (!Objects.equals(topInventory, inventory))
        removeViewers.add(viewer);
    });
    removeViewers.forEach(viewers::remove);
    return !removeViewers.isEmpty() && revalidateTask();
  }

  @CanIgnoreReturnValue
  protected boolean start() {
    if (task != null)
      return false;
    synchronized (lock) {
      if (task != null)
        return false;
      // render the inventory with viewer check
      this.task = Bukkit.getScheduler().runTaskTimer(plugin, this::updateInventory,
          updateInterval.toTicks(), updateInterval.toTicks());
      Bukkit.getPluginManager().registerEvents(listener, plugin);
      return true;
    }
  }

  @CanIgnoreReturnValue
  protected boolean stop() {
    if (task == null)
      return false;
    synchronized (lock) {
      if (task == null)
        return false;
      task.cancel();
      task = null;
      viewers.clear();
      updateTicker.reset();
      HandlerList.unregisterAll(listener);
      return true;
    }
  }

  @CanIgnoreReturnValue
  private boolean revalidateTask() {
    if (viewers.isEmpty() || inventory == null)
      return stop();
    return false;
  }

  private void createInventory() {
    Preconditions.checkNotNull(content, "Content is undefined");
    updateTicker.reset(); // reset first, to avoid automatic `checkForViewers`
    this.inventory = Bukkit.createInventory(null, content.getDimensions().size(), title);
    viewers.forEach((viewer) -> viewer.openInventory(inventory));
  }

  public @Nullable InventoryContentView getContent() {
    return content;
  }

  public @Nullable Inventory getInventory() {
    return inventory;
  }

  public WeakHashSet<Player> getViewers() {
    return viewers;
  }

  public TickDuration getUpdateInterval() {
    return updateInterval;
  }

  @Override
  public Ticker getUpdateTicker() {
    return updateTicker;
  }

  public String getTitle() {
    return title;
  }

  public Plugin getPlugin() {
    return plugin;
  }
}
