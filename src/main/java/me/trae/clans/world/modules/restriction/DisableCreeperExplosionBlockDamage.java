package me.trae.clans.world.modules.restriction;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

public class DisableCreeperExplosionBlockDamage extends SpigotListener<Clans, WorldManager> {

    public DisableCreeperExplosionBlockDamage(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Creeper)) {
            return;
        }

        event.setCancelled(true);
    }
}