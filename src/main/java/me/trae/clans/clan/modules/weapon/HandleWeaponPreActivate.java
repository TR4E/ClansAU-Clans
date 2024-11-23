package me.trae.clans.clan.modules.weapon;

import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import me.trae.core.weapon.events.WeaponPreActivateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleWeaponPreActivate extends SpigotListener<Clans, ClanManager> {

    public HandleWeaponPreActivate(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onWeaponPreActivate(final WeaponPreActivateEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        if (this.getManager().canCast(player)) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.message(player, "Clans", "You cannot cast abilities here!");
    }
}