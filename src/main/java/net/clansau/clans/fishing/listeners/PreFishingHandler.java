package net.clansau.clans.fishing.listeners;

import net.clansau.clans.fishing.FishingManager;
import net.clansau.clans.fishing.events.CustomFishingEvent;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilItem;
import net.clansau.core.utility.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

public class PreFishingHandler extends CoreListener<FishingManager> {

    public PreFishingHandler(final FishingManager manager) {
        super(manager, "Pre Fishing Handler");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerFish(final PlayerFishEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getState().equals(PlayerFishEvent.State.FISHING)) {
            UtilItem.setBiteTime(e.getHook(), UtilMath.randomInt(20, 60));
        }
        final Entity caught = e.getCaught();
        if (caught == null) {
            return;
        }
        if (caught instanceof ArmorStand) {
            e.setCancelled(true);
            return;
        }
        final Player player = e.getPlayer();
        if (caught instanceof Player && caught.equals(player)) {
            return;
        }
        if (!(caught.getType().equals(EntityType.DROPPED_ITEM))) {
            return;
        }
        e.setExpToDrop(0);
        caught.remove();
        Bukkit.getServer().getPluginManager().callEvent(new CustomFishingEvent(player, caught));
    }
}