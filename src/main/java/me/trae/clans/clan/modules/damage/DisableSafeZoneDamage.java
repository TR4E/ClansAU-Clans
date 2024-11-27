package me.trae.clans.clan.modules.damage;

import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Collections;

public class DisableSafeZoneDamage extends SpigotListener<Clans, ClanManager> {

    public DisableSafeZoneDamage(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPreDamage(final CustomPreDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getDamagee() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        final Player damagee = event.getDamageeByClass(Player.class);
        final Player damager = event.getDamagerByClass(Player.class);

        if (!(this.getManager().isSafeByPlayer(damagee) && this.getManager().isSafeByPlayer(damager))) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.simpleMessage(damager, "Clans", "You cannot harm <var>.", Collections.singletonList(event.getDamageeName()));
    }
}