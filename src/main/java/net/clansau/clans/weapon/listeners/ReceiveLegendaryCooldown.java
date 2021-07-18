package net.clansau.clans.weapon.listeners;

import net.clansau.clans.weapon.WeaponManager;
import net.clansau.core.framework.Primitive;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.weapon.events.LegendaryReceiveEvent;
import org.bukkit.event.EventHandler;

public class ReceiveLegendaryCooldown extends CoreListener<WeaponManager> {

    public ReceiveLegendaryCooldown(final WeaponManager manager) {
        super(manager, "Receive Legendary Cooldown");
        addPrimitive("Cooldown", new Primitive<>(86400000)); // 1 Day Cooldown
    }

    @EventHandler
    public void onLegendaryCommand(final LegendaryReceiveEvent e) {
        if (e.isCancelled() || e.hasCooldown()) {
            return;
        }
        e.setCooldown(getPrimitiveCasted(Integer.class, "Cooldown"));
    }
}