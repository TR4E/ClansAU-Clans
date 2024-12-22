package me.trae.clans.clan.modules.damage;

import me.trae.api.combat.CombatManager;
import me.trae.api.damage.events.PlayerCaughtEntityEvent;
import me.trae.api.damage.events.damage.CustomPreDamageEvent;
import me.trae.clans.Clans;
import me.trae.clans.clan.ClanManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Collections;

public class DisableSafeZoneDamage extends SpigotListener<Clans, ClanManager> {

    public DisableSafeZoneDamage(final ClanManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPreDamageWithDamagee(final CustomPreDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getDamagee() instanceof Player)) {
            return;
        }

        if (event.hasDamager()) {
            return;
        }

        if (!(this.getManager().isSafeByPlayer(event.getDamageeByClass(Player.class)))) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCustomPreDamageWithDamager(final CustomPreDamageEvent event) {
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

        if (!(this.isValid(damagee, damager))) {
            return;
        }

        event.setCancelled(true);

        if (event.getProjectile() instanceof FishHook) {
            return;
        }

        UtilMessage.simpleMessage(damager, "Clans", "You cannot harm <var>.", Collections.singletonList(event.getDamageeName()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCaughtEntity(final PlayerCaughtEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getCaught() instanceof Player)) {
            return;
        }

        final Player damager = event.getPlayer();
        final Player damagee = event.getCaughtByClass(Player.class);

        if (!(this.isValid(damagee, damager))) {
            return;
        }

        event.setCancelled(true);
    }

    private boolean isValid(final Player damagee, final Player damager) {
        final boolean damageeInSafeZone = this.getManager().isSafeByLocation(damagee.getLocation());
        final boolean damagerInSafeZone = this.getManager().isSafeByLocation(damager.getLocation());

        if (!(damageeInSafeZone) && !(damagerInSafeZone)) {
            return false;
        }

        final ClientManager clientManager = this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class);

        if (clientManager.getClientByPlayer(damager).isAdministrating()) {
            return false;
        }

        if (!(clientManager.getClientByPlayer(damagee).isAdministrating())) {
            final CombatManager combatManager = this.getInstanceByClass(Core.class).getManagerByClass(CombatManager.class);

            final boolean damageeInCombat = combatManager.isCombatByPlayer(damagee);
            final boolean damagerInCombat = combatManager.isCombatByPlayer(damager);

            if (damageeInCombat && damagerInCombat) {
                return false;
            }

            if (!(damagerInCombat) && damageeInCombat) {
                return false;
            }
        }

        return true;
    }
}