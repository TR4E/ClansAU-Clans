package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotSubListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

public class HandleAgilityHelmetFallDamage extends SpigotSubListener<Clans, AgilityHelmet> {

    public HandleAgilityHelmetFallDamage(final AgilityHelmet module) {
        super(module);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomPreDamage(final CustomPreDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        if (!(event.getDamagee() instanceof Player)) {
            return;
        }

        if (!(this.getModule().isUsing(event.getDamageeByClass(Player.class)))) {
            return;
        }

        event.setCancelled(true);
    }
}