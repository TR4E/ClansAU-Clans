package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class ClanGamemodeHandler extends CoreListener<WorldManager> {

    public ClanGamemodeHandler(final WorldManager manager) {
        super(manager, "Clan Gamemode Handler");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent e) {
        this.handleGamemode(e.getPlayer(), e.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        this.handleGamemode(e.getPlayer(), e.getPlayer().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(final PlayerTeleportEvent e) {
        this.handleGamemode(e.getPlayer(), e.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(final PlayerRespawnEvent e) {
        this.handleGamemode(e.getPlayer(), e.getRespawnLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleEnter(final VehicleEnterEvent e) {
        if (!(e.getEntered() instanceof Player)) {
            return;
        }
        this.handleGamemode((Player) e.getEntered(), e.getEntered().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleExit(final VehicleExitEvent e) {
        if (!(e.getExited() instanceof Player)) {
            return;
        }
        this.handleGamemode((Player) e.getExited(), e.getExited().getLocation());
    }

    private void handleGamemode(final Player player, final Location location) {
        final Clan clan = getManager().getClanManager().getClan(player.getUniqueId());
        final Clan land = getManager().getClanManager().getClan(location);
        final ClanRelation relation = getManager().getClanManager().getClanRelation(clan, land);
        if (relation.equals(ClanRelation.SELF) || relation.equals(ClanRelation.PILLAGE)) {
            if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            return;
        }
        if (land == null || land.getName().equalsIgnoreCase("fields")) {
            if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            return;
        }
        if (!(land.equals(clan))) {
            if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                player.setGameMode(GameMode.ADVENTURE);
            }
        }
    }

    @Override
    public void initializeModule() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            this.handleGamemode(player, player.getLocation());
        }
    }

    @Override
    public void shutdownModule() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }
    }
}