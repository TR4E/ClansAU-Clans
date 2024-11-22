package me.trae.clans.tnt.modules;

import me.trae.clans.Clans;
import me.trae.clans.tnt.TntManager;
import me.trae.clans.tnt.events.TNTExplodeEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilServer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

public class HandlePreTNTExplode extends SpigotListener<Clans, TntManager> {

    public HandlePreTNTExplode(final TntManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof TNTPrimed)) {
            return;
        }

        event.setCancelled(true);

        final Location location = event.getLocation();

        final TNTExplodeEvent tntExplodeEvent = new TNTExplodeEvent(location, this.getManager().radius);
        UtilServer.callEvent(tntExplodeEvent);
        if (tntExplodeEvent.isCancelled()) {
            return;
        }


        for (final Block block : tntExplodeEvent.getBlocks()) {
            block.breakNaturally();
        }
    }
}