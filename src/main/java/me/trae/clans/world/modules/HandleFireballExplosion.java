package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilJava;
import org.bukkit.Effect;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

public class HandleFireballExplosion extends SpigotListener<Clans, WorldManager> {

    public HandleFireballExplosion(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Fireball)) {
            return;
        }

        final Fireball fireball = UtilJava.cast(Fireball.class, event.getEntity());

        event.setCancelled(true);

        fireball.getWorld().playEffect(fireball.getLocation(), Effect.EXPLOSION_HUGE, 100);
    }
}