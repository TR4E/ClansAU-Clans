package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.perk.PerkManager;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

public class HandleAgilityHelmetFallDamage extends SpigotListener<Clans, PerkManager> {

    private final AgilityHelmet PERK;

    public HandleAgilityHelmetFallDamage(final PerkManager manager) {
        super(manager);

        this.PERK = manager.getModuleByClass(AgilityHelmet.class);
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

        if (!(this.PERK.isUsing(event.getDamageeByClass(Player.class)))) {
            return;
        }

        event.setCancelled(true);
    }
}