package me.trae.clans.clan.modules.weapon;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.weapon.events.WeaponLocationEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleWeaponLocation extends SpigotListener<Clans, ClanManager> {

    public HandleWeaponLocation(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onWeaponLocation(final WeaponLocationEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (this.getManager().canCast(event.getLocation())) {
            return;
        }

        event.setCancelled(true);
    }
}