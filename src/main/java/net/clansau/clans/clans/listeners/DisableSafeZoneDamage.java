package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.general.combat.events.CustomDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

public class DisableSafeZoneDamage extends CoreListener<ClanManager> {

    public DisableSafeZoneDamage(final ClanManager manager) {
        super(manager, "Disable Safe Zone Damage");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSafeZoneDamage(final CustomDamageEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!(e.getCause().equals(EntityDamageEvent.DamageCause.FALL) || e.getCause().equals(EntityDamageEvent.DamageCause.DROWNING))) {
            return;
        }
        final Player damagee = e.getDamageePlayer();
        final Clan land = getManager().getClan(damagee.getLocation());
        if (land == null) {
            return;
        }
        if (!(land instanceof AdminClan && ((AdminClan) land).isSafe())) {
            return;
        }
        e.setCancelled(true);
    }
}