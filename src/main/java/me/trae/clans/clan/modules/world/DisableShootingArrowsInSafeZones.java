package me.trae.clans.clan.modules.world;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

public class DisableShootingArrowsInSafeZones extends SpigotListener<Clans, ClanManager> {

    public DisableShootingArrowsInSafeZones(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityShootBow(final EntityShootBowEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        final Player player = UtilJava.cast(Player.class, event.getEntity());

        if (!(this.getManager().isSafeByPlayer(player))) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.message(player, "Clans", "You cannot shoot arrows in Safe Zones!");
    }
}