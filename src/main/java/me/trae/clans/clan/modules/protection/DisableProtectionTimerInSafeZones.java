package me.trae.clans.clan.modules.protection;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.gamer.events.ProtectionCheckEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class DisableProtectionTimerInSafeZones extends SpigotListener<Clans, ClanManager> {

    public DisableProtectionTimerInSafeZones(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onProtectionCheck(final ProtectionCheckEvent event) {
        final Player player = event.getGamer().getPlayer();

        if (!(this.getManager().isSafeByLocation(player.getLocation()))) {
            return;
        }

        event.setCancelled(true);
    }
}