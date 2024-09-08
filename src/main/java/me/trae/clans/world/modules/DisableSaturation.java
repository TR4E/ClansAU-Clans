package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DisableSaturation extends SpigotListener<Clans, WorldManager> {

    public DisableSaturation(final WorldManager manager) {
        super(manager);
    }

    private void apply(final Player player) {
        player.setFoodLevel(20);
        player.setSaturation(0.0F);
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        event.setCancelled(true);

        if (event.getEntity() instanceof Player) {
            this.apply(UtilJava.cast(Player.class, event.getEntity()));
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.apply(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.apply(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        UtilServer.runTaskLater(Clans.class, false, 1L, () -> this.apply(event.getPlayer()));
    }
}