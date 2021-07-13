package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.general.combat.events.CustomDeathEvent;
import net.clansau.core.utility.UtilMessage;
import net.clansau.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HandleDominance extends CoreListener<ClanManager> {

    public HandleDominance(final ClanManager manager) {
        super(manager, "Handle Dominance");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCustomDeath(final CustomDeathEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() == null || e.getKiller() == null) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (!(e.getKiller() instanceof Player)) {
            return;
        }
        final Player killer = e.getKillerPlayer();
        final Player player = e.getEntityPlayer();
        final Clan kClan = getManager().getClan(killer.getUniqueId());
        final Clan pClan = getManager().getClan(player.getUniqueId());
        this.handleFunction(pClan, kClan);
    }

    public void handleFunction(final Clan pClan, final Clan kClan) {
        if (pClan == null || kClan == null) {
            return;
        }
        if (!(pClan.isEnemied(kClan))) {
            return;
        }
        if (kClan.getEnemiesMap().get(pClan.getName()) >= 15) {
            this.handlePillaging(pClan, kClan);
            return;
        }
        this.handleDominance(pClan, kClan);
    }

    private void handlePillaging(final Clan pClan, final Clan kClan) {
        kClan.getEnemiesMap().remove(pClan.getName());
        pClan.getEnemiesMap().remove(kClan.getName());
        getManager().getRepository().updateEnemies(kClan);
        getManager().getRepository().updateEnemies(pClan);
        kClan.setPoints(kClan.getPoints() + 1);
        getManager().getRepository().updatePoints(kClan);
        kClan.getPillagingMap().put(pClan.getName(), System.currentTimeMillis() + getManager().getMaxPillageLength());
        kClan.soundClan(Sound.NOTE_PLING, 1.0F, 1.0F, null);
        pClan.soundClan(Sound.NOTE_PLING, 1.0F, 1.0F, null);
        kClan.messageClan("Clans", "Your Clan is now pillaging " + getManager().getClanRelation(kClan, pClan).getSuffix() + getManager().getName(pClan, true) + ChatColor.GRAY + " for " + ChatColor.GREEN + UtilTime.getTime(getManager().getMaxPillageLength(), UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + ".", null);
        pClan.messageClan("Clans", "Your Clan is now being pillaged by " + getManager().getClanRelation(pClan, kClan).getSuffix() + getManager().getName(kClan, true) + ChatColor.GRAY + " for " + ChatColor.GREEN + UtilTime.getTime(getManager().getMaxPillageLength(), UtilTime.TimeUnit.BEST, 1) + ChatColor.GRAY + ".", null);
        for (final Player other : Bukkit.getOnlinePlayers()) {
            if (kClan.isMember(other.getUniqueId()) || pClan.isMember(other.getUniqueId())) {
                continue;
            }
            final Clan oClan = getManager().getClan(other.getUniqueId());
            UtilMessage.message(other, "Clans", getManager().getClanRelation(oClan, kClan).getSuffix() + getManager().getName(kClan, true) + ChatColor.GRAY + " has conquered " + getManager().getClanRelation(oClan, pClan).getSuffix() + getManager().getName(pClan, true) + ChatColor.GRAY + ".");
        }
    }

    private void handleDominance(final Clan pClan, final Clan kClan) {
        if (pClan.getEnemiesMap().get(kClan.getName()) <= kClan.getEnemiesMap().get(pClan.getName())) {
            kClan.getEnemiesMap().put(pClan.getName(), kClan.getEnemiesMap().get(pClan.getName()) + 1);
            getManager().getRepository().updateEnemies(kClan);
            kClan.messageClan("Clans", "Your Clan gained a Dominance against " + getManager().getClanRelation(pClan, kClan).getSuffix() + getManager().getName(kClan, true) + ChatColor.GRAY + ". " + kClan.getDomString(pClan), null);
            pClan.messageClan("Clans", "Your Clan lost a Dominance against " + getManager().getClanRelation(kClan, pClan).getSuffix() + getManager().getName(pClan, true) + ChatColor.GRAY + ". " + pClan.getDomString(kClan), null);
        } else if (pClan.getEnemiesMap().get(kClan.getName()) > kClan.getEnemiesMap().get(pClan.getName())) {
            pClan.getEnemiesMap().put(kClan.getName(), pClan.getEnemiesMap().get(kClan.getName()) - 1);
            getManager().getRepository().updateEnemies(pClan);
            kClan.messageClan("Clans", "Your Clan recovered a Dominance against " + getManager().getClanRelation(pClan, kClan).getSuffix() + getManager().getName(kClan, true) + ChatColor.GRAY + ". " + kClan.getDomString(pClan), null);
            pClan.messageClan("Clans", "Your Clan lost a Dominance against " + getManager().getClanRelation(kClan, pClan).getSuffix() + getManager().getName(pClan, true) + ChatColor.GRAY + ". " + pClan.getDomString(kClan), null);
        }
    }
}
