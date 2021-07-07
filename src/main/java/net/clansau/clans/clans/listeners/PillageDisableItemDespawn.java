package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ItemDespawnEvent;

public class PillageDisableItemDespawn extends CoreListener<ClanManager> {

    public PillageDisableItemDespawn(final ClanManager manager) {
        super(manager, "Pillage Disable Item Despawn");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemDespawn(final ItemDespawnEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Clan land = getManager().getClan(e.getLocation());
        if (land == null) {
            return;
        }
        if (!(land.isBeingPillaged())) {
            return;
        }
        e.setCancelled(true);
    }
}