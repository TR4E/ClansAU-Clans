package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.general.combat.events.PlayerSuicideEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class SuicideInEnemyTerritory extends CoreListener<ClanManager> {

    public SuicideInEnemyTerritory(final ClanManager manager) {
        super(manager, "Suicide In Enemy Territory");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerSuicide(final PlayerSuicideEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            return;
        }
        final Clan land = getManager().getClan(player.getLocation());
        if (land == null) {
            return;
        }
        if (!(clan.isEnemied(land))) {
            return;
        }
        getManager().getModule(HandleDominance.class).handleFunction(clan, land);
    }
}