package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.scoreboard.events.ScoreboardUpdateEvent;
import net.clansau.core.utility.TitleManager;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        final Player player = e.getPlayer();
        this.displayOwner(player, getManager().getTerritoryName(player, e.getTo()));
        final ScoreboardUpdateEvent event = new ScoreboardUpdateEvent(player);
        event.setLocation(e.getTo());
        Bukkit.getServer().getPluginManager().callEvent(event);
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
        final Player player = e.getPlayer();
        this.displayOwner(player, getManager().getTerritoryName(player, e.getTo()));
        final ScoreboardUpdateEvent event = new ScoreboardUpdateEvent(player);
        event.setLocation(e.getTo());
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    private void displayOwner(final Player player, final String text) {
        getInstance().getManager(TitleManager.class).sendPlayer(player, " ", text, 2);
        UtilMessage.message(player, "Territory", text);
    }
}