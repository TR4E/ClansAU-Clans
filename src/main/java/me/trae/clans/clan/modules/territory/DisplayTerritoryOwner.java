package me.trae.clans.clan.modules.territory;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.UtilTitle;
import me.trae.core.utility.objects.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DisplayTerritoryOwner extends SpigotListener<Clans, ClanManager> {

    @ConfigInject(type = Boolean.class, name = "Title-Enabled", defaultValue = "true")
    private boolean titleEnabled;

    @ConfigInject(type = Long.class, name = "Title-Duration", defaultValue = "2000")
    private long titleDuration;

    public DisplayTerritoryOwner(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Location fromLocation = event.getFrom();
        final Location toLocation = event.getTo();

        if (fromLocation.getBlock() == toLocation.getBlock()) {
            return;
        }

        final Clan fromTerritoryClan = this.getManager().getClanByLocation(fromLocation);
        final Clan toTerritoryClan = this.getManager().getClanByLocation(toLocation);

        if (!(this.canDisplay(fromLocation, toLocation, fromTerritoryClan, toTerritoryClan))) {
            return;
        }

        this.display(event.getPlayer(), toLocation, toTerritoryClan);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Location fromLocation = event.getFrom();
        final Location toLocation = event.getTo();

        if (fromLocation.getBlock() == toLocation.getBlock()) {
            return;
        }

        final Clan fromTerritoryClan = this.getManager().getClanByLocation(fromLocation);
        final Clan toTerritoryClan = this.getManager().getClanByLocation(toLocation);

        if (!(this.canDisplay(fromLocation, toLocation, fromTerritoryClan, toTerritoryClan))) {
            return;
        }

        this.display(event.getPlayer(), toLocation, toTerritoryClan);
    }

    private boolean canDisplay(final Location fromLocation, final Location toLocation, final Clan fromTerritoryClan, final Clan toTerritoryClan) {
        if (fromLocation == toLocation) {
            return false;
        }

        if (fromTerritoryClan == null && toTerritoryClan == null) {
            return false;
        }

        if (this.getManager().isSafeByLocation(fromLocation) && this.getManager().isSafeByLocation(toLocation)) {
            return false;
        }

        if (fromTerritoryClan == toTerritoryClan) {
            return false;
        }

        return true;
    }

    private void display(final Player player, final Location location, final Clan territoryClan) {
        final Clan playerClan = this.getManager().getClanByPlayer(player);

        if (this.titleEnabled) {
            final Pair<String, String> pair = this.getManager().getTerritoryClanNameForTitle(playerClan, territoryClan);

            UtilTitle.sendTitle(player, pair.getLeft(), pair.getRight(), true, this.titleDuration);
        }

        UtilMessage.simpleMessage(player, "Territory", this.getManager().getTerritoryClanNameForChat(playerClan, territoryClan, location));

        UtilServer.callEvent(new ScoreboardUpdateEvent(player));
    }
}