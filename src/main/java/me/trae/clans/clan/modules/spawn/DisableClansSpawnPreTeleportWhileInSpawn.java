package me.trae.clans.clan.modules.spawn;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.utility.UtilClans;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.spawn.events.SpawnPreTeleportEvent;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class DisableClansSpawnPreTeleportWhileInSpawn extends SpigotListener<Clans, ClanManager> {

    public DisableClansSpawnPreTeleportWhileInSpawn(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onSpawnPreTeleport(final SpawnPreTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        if (!(UtilClans.isSpawnClan(this.getManager().getClanByLocation(player.getLocation())))) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.message(player, "Spawn", "You are already at Spawn!");
    }
}