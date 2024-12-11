package me.trae.clans.gamer.modules.protection;

import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.gamer.GamerManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.Collections;

public class HandleProtectionDamage extends SpigotListener<Clans, GamerManager> {

    public HandleProtectionDamage(final GamerManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCustomPreDamage(final CustomPreDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getDamagee() instanceof Player)) {
            return;
        }

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        final Player damagee = event.getDamageeByClass(Player.class);
        final Player damager = event.getDamagerByClass(Player.class);

        if (this.getManager().getGamerByPlayer(damager).hasProtection()) {
            event.setCancelled(true);

            if (this.canInform(event)) {
                UtilMessage.message(damager, "Protection", "You cannot damage others while you still have protection.");
                UtilMessage.simpleMessage(damager, "Protection", "Type <green>/protection remove</green> to remove your protection.");
            }
            return;
        }

        if (this.getManager().getGamerByPlayer(damagee).hasProtection()) {
            event.setCancelled(true);

            if (this.canInform(event)) {
                UtilMessage.simpleMessage(damager, "Protection", "<var> cannot be damaged due to protection.", Collections.singletonList(event.getDamageeName()));
            }
        }
    }

    private boolean canInform(final CustomPreDamageEvent event) {
        return Arrays.asList(EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityDamageEvent.DamageCause.PROJECTILE).contains(event.getCause());
    }
}