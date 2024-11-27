package me.trae.clans.clan.modules.world;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class DisableNaturalCreatureSpawningInAdminClanTerritory extends SpigotListener<Clans, ClanManager> {

    public DisableNaturalCreatureSpawningInAdminClanTerritory(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        final Clan territoryClan = this.getManager().getClanByLocation(event.getLocation());
        if (territoryClan == null) {
            return;
        }

        if (!(territoryClan.isAdmin())) {
            return;
        }

        event.setCancelled(true);
    }
}