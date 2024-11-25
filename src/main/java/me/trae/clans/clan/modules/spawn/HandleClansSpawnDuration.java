package me.trae.clans.clan.modules.spawn;

import me.trae.clans.Clans;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.utility.UtilClans;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.spawn.events.SpawnDurationEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HandleClansSpawnDuration extends SpigotListener<Clans, ClanManager> {

    @ConfigInject(type = Long.class, path = "Wilderness-Duration", defaultValue = "30_000")
    private long wildernessDuration;

    public HandleClansSpawnDuration(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onSpawnDuration(final SpawnDurationEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        final Clan territoryClan = this.getManager().getClanByLocation(player.getLocation());
        if (UtilClans.isSpawnClan(territoryClan)) {
            event.setDuration(0L);
            return;
        }

        event.setDuration(this.wildernessDuration);
    }
}