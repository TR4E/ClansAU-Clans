package me.trae.clans.gamer.modules.protection;

import me.trae.clans.Clans;
import me.trae.clans.gamer.GamerManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.weapon.events.WeaponFriendlyFireEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleProtectionWeaponFriendlyFire extends SpigotListener<Clans, GamerManager> {

    public HandleProtectionWeaponFriendlyFire(final GamerManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWeaponFriendlyFire(final WeaponFriendlyFireEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(this.getManager().getGamerByPlayer(event.getTarget()).hasProtection())) {
            return;
        }

        event.setCancelled(true);
    }
}