package net.clansau.clans.weapon.listeners;

import net.clansau.clans.weapon.WeaponManager;
import net.clansau.core.framework.Primitive;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.weapon.events.LegendaryCommandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ReceiveLegendaryCooldown extends CoreListener<WeaponManager> {

    public ReceiveLegendaryCooldown(final WeaponManager manager) {
        super(manager, "Receive Legendary Cooldown");
        addPrimitive("Cooldown", new Primitive<>(86400000)); // 1 Day Cooldown
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLegendaryCommand(final LegendaryCommandEvent e) {
        if (e.isCancelled() || e.hasCooldown()) {
            return;
        }
        e.setCooldown(getPrimitiveCasted(Long.class, "Cooldown"));
    }
}