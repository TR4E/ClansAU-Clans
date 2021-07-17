package net.clansau.clans.world.listeners;

import net.clansau.champions.classes.role.events.KitCommandEvent;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.framework.Primitive;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ReceiveKitCooldown extends CoreListener<WorldManager> {

    public ReceiveKitCooldown(final WorldManager manager) {
        super(manager, "Receive Kit Cooldown");
        addPrimitive("Cooldown", new Primitive<>(86400000)); // 1 Day Cooldown
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKitCooldown(final KitCommandEvent e) {
        if (e.isCancelled() || e.hasCooldown()) {
            return;
        }
        e.setCooldown(getPrimitiveCasted(Integer.class, "Cooldown"));
    }
}