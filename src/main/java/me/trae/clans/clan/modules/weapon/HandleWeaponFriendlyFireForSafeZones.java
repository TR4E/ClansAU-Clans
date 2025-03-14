package me.trae.clans.clan.modules.weapon;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.weapon.events.WeaponFriendlyFireEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleWeaponFriendlyFireForSafeZones extends SpigotListener<Clans, ClanManager> {

    public HandleWeaponFriendlyFireForSafeZones(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWeaponFriendlyFire(final WeaponFriendlyFireEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(this.getManager().isSafeByPlayer(event.getTarget()))) {
            return;
        }

        event.setCancelled(true);
    }
}