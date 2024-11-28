package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

public class DisableHopperMoveItem extends SpigotListener<Clans, WorldManager> {

    public DisableHopperMoveItem(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryMoveItem(final InventoryMoveItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getSource().getType() == InventoryType.HOPPER) {
            event.setCancelled(true);
        }

        if (event.getDestination().getType() == InventoryType.HOPPER) {
            event.setCancelled(true);
        }
    }
}