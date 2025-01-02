package me.trae.clans.clan.modules.territory;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.utility.UtilClans;
import me.trae.core.Core;
import me.trae.core.client.Client;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilServer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HandleAdventureModeInClanTerritory extends SpigotListener<Clans, ClanManager> {

    private final Set<UUID> PLAYERS = new HashSet<>();

    public HandleAdventureModeInClanTerritory(final ClanManager manager) {
        super(manager);
    }

    @Override
    public boolean isRegistered() {
        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent event) {
        this.handleGamemode(event.getPlayer(), event.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.handleGamemode(event.getPlayer(), event.getPlayer().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.setGamemode(event.getPlayer(), GameMode.ADVENTURE, GameMode.SURVIVAL);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }

        this.handleGamemode(event.getPlayer(), event.getTo());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        this.handleGamemode(event.getPlayer(), event.getRespawnLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleEnter(final VehicleEnterEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntered() instanceof Player)) {
            return;
        }

        this.handleGamemode(UtilJava.cast(Player.class, event.getEntered()), event.getEntered().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleExit(final VehicleExitEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getExited() instanceof Player)) {
            return;
        }

        this.handleGamemode(UtilJava.cast(Player.class, event.getExited()), event.getExited().getLocation());
    }

    private void handleGamemode(final Player player, final Location location) {
        final Client client = this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player);

        final Clan playerClan = this.getManager().getClanByPlayer(player);
        final Clan territoryClan = this.getManager().getClanByLocation(location);

        boolean condition = client.isAdministrating();

        if (!(condition)) {
            condition = territoryClan != null && playerClan != null && (playerClan == territoryClan || playerClan.isPillageByClan(territoryClan));
            condition |= territoryClan == null || UtilClans.isFieldsClan(territoryClan);
        }

        if (condition) {
            this.setGamemode(player, GameMode.ADVENTURE, GameMode.SURVIVAL);
            return;
        }

        this.setGamemode(player, GameMode.SURVIVAL, GameMode.ADVENTURE);

        this.PLAYERS.add(player.getUniqueId());
    }

    private void setGamemode(final Player player, final GameMode currentGameMode, final GameMode newGameMode) {
        if (player.getGameMode() == currentGameMode) {
            player.setGameMode(newGameMode);
        }
    }

    @Override
    public void onInitialize() {
        for (final Player targetPlayer : UtilServer.getOnlinePlayers()) {
            this.handleGamemode(targetPlayer, targetPlayer.getLocation());
        }
    }

    @Override
    public void onShutdown() {
        this.PLAYERS.removeIf(uuid -> {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                this.setGamemode(player, GameMode.ADVENTURE, GameMode.SURVIVAL);
            }

            return true;
        });
    }
}