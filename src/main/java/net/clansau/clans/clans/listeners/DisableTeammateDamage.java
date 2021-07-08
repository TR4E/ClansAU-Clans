package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.general.combat.events.CustomDamageEvent;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class DisableTeammateDamage extends CoreListener<ClanManager> {

    public DisableTeammateDamage(final ClanManager manager) {
        super(manager, "Disable Teammate Damage");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeammateDamage(final CustomDamageEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getDamagee() instanceof Player)) {
            return;
        }
        final Player damagee = e.getDamageePlayer();
        final Player damager = e.getDamagerPlayer();
        if (getManager().canHurt(damager, damagee)) {
            return;
        }
        final Clan damageeClan = getManager().getClan(damagee.getUniqueId());
        final Clan damagerClan = getManager().getClan(damager.getUniqueId());
        e.setCancelled(true);
        UtilMessage.message(damager, "Clans", "You cannot harm " + getManager().getClanRelation(damagerClan, damageeClan).getSuffix() + damagee.getName() + ChatColor.GRAY + ".");
    }
}