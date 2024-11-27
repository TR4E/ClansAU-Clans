package me.trae.clans.clan.modules.world;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.LeavesDecayEvent;

public class DisableLeavesDecayInAdminClanTerritory extends SpigotListener<Clans, ClanManager> {

    public DisableLeavesDecayInAdminClanTerritory(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLeavesDecay(final LeavesDecayEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan territoryClan = this.getManager().getClanByLocation(event.getBlock().getLocation());
        if (territoryClan == null) {
            return;
        }

        if (!(territoryClan.isAdmin())) {
            return;
        }

        event.setCancelled(true);
    }
}