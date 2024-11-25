package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;

public class RemoveArrowsOnHit extends SpigotListener<Clans, WorldManager> {

    public RemoveArrowsOnHit(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }

        event.getEntity().remove();
    }
}