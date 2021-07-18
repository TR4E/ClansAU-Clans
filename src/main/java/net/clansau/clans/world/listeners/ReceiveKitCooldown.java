package net.clansau.clans.world.listeners;

import net.clansau.champions.classes.role.events.KitReceiveEvent;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.framework.Primitive;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.framework.recharge.Recharge;
import net.clansau.core.framework.recharge.RechargeManager;
import org.bukkit.event.EventHandler;

import java.util.Map;

public class ReceiveKitCooldown extends CoreListener<WorldManager> {

    public ReceiveKitCooldown(final WorldManager manager) {
        super(manager, "Receive Kit Cooldown");
        addPrimitive("Cooldown", new Primitive<>(86400000)); // 1 Day Cooldown
    }

    @EventHandler
    public void onKitCooldown(final KitReceiveEvent e) {
        if (e.isCancelled() || e.hasCooldown()) {
            return;
        }
        e.setCooldown(getPrimitiveCasted(Integer.class, "Cooldown"));
    }

    @Override
    protected void shutdownModule() {
        for (final Map<String, Recharge> map : getInstance().getManager(RechargeManager.class).getCooldowns().values()) {
            for (final Recharge recharge : map.values()) {
                if (!(recharge.getName().equals("Kit Command"))) {
                    continue;
                }
                map.remove(recharge.getName());
            }
        }
    }
}