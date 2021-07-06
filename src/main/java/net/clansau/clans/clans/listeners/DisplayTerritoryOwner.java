package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DisplayTerritoryOwner extends CoreListener<ClanManager> {

    public DisplayTerritoryOwner(final ClanManager manager) {
        super(manager, "Display Territory Owner");
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            return;
        }
        final Clan to = getManager().getClan(e.getTo());
        final Clan from = getManager().getClan(e.getFrom());
        if (to == null && from == null) {
            return;
        }
        if (!(from == null || to == null || (getManager().isSafe(e.getFrom()) && !(getManager().isSafe(e.getTo())) || (!(getManager().isSafe(e.getFrom())) && getManager().isSafe(e.getTo())) || !(from.equals(to))))) {
            return;
        }
        getManager().displayOwner(e.getPlayer(), e.getTo());
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Clan to = getManager().getClan(e.getTo());
        final Clan from = getManager().getClan(e.getFrom());
        if (to == null || from == null || (to.equals(from))) {
            return;
        }
        getManager().displayOwner(e.getPlayer(), e.getTo());
    }
}