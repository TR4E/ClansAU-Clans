package me.trae.clans.clan.modules.tnt;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.tnt.events.TNTExplodeEvent;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashSet;
import java.util.Set;

public class HandleClanTerritoryTntProtection extends SpigotListener<Clans, ClanManager> {

    @ConfigInject(type = Long.class, path = "Wilderness-BlockRestore-Duration", defaultValue = "600_000")
    private long wildernessBlockRestoreDuration;

    public HandleClanTerritoryTntProtection(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTNTExplode(final TNTExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Set<Clan> clans = new HashSet<>();

        event.getBlocks().removeIf(block -> {
            final Clan territoryClan = this.getManager().getClanByLocation(block.getLocation());
            if (territoryClan != null) {
                if (territoryClan.isTNTProtected(this.getManager())) {
                    return true;
                }

                clans.add(territoryClan);
            }

            return false;
        });

        for (final Clan clan : clans) {
            clan.setLastTNTed(System.currentTimeMillis());
            this.getManager().getRepository().updateData(clan, ClanProperty.LAST_TNTED);
        }
    }
}