package net.clansau.clans.clans.listeners;

import net.clansau.champions.classes.skill.events.SkillEffectEvent;
import net.clansau.champions.classes.skill.events.SkillFriendlyFireEvent;
import net.clansau.champions.classes.skill.events.SkillLocationEvent;
import net.clansau.champions.classes.skill.events.SkillPreActivateEvent;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.weapon.events.WeaponEffectEvent;
import net.clansau.core.weapon.events.WeaponFriendlyFireEvent;
import net.clansau.core.weapon.events.WeaponPreUseEvent;
import net.clansau.core.weapon.events.WeaponUsingEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ChampionsListener extends CoreListener<ClanManager> {

    public ChampionsListener(final ClanManager manager) {
        super(manager, "Champions Listener");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSkillEffect(final SkillEffectEvent e) {
        if (e.getPlayer() == null || e.getTarget() == null) {
            return;
        }
        e.setPlayerName(this.getName(e.getPlayer(), e.getTarget()));
        e.setTargetName(this.getName(e.getTarget(), e.getPlayer()));
    }

    private String getName(final Player player, final Player target) {
        final Clan pClan = getManager().getClan(player.getUniqueId());
        final Clan tClan = getManager().getClan(target.getUniqueId());
        return getManager().getClanRelation(tClan, pClan).getSuffix() + player.getName();
    }

    @EventHandler
    public void onSkillFriendlyFire(final SkillFriendlyFireEvent e) {
        final Player player = e.getPlayer();
        final Player target = e.getTarget();
        if (this.canHurt(player, target)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onSkillLocation(final SkillLocationEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Location location = e.getLocation();
        if (getManager().canCast(location)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onCanCastSkill(final SkillPreActivateEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        if (getManager().canCast(player)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onWeaponPreUse(final WeaponPreUseEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        if (getManager().canCast(player)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onWeaponUsing(final WeaponUsingEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        if (getManager().canCast(player.getLocation())) {
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isAdministrating()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWeaponEffect(final WeaponEffectEvent e) {
        if (e.getPlayer() == null || e.getTarget() == null) {
            return;
        }
        e.setPlayerName(this.getName(e.getPlayer(), e.getTarget()));
        e.setTargetName(this.getName(e.getTarget(), e.getPlayer()));
    }

    @EventHandler
    public void onWeaponFriendlyFire(final WeaponFriendlyFireEvent e) {
        final Player player = e.getPlayer();
        final Player target = e.getTarget();
        if (this.canHurt(player, target)) {
            return;
        }
        e.setCancelled(true);
    }

    private boolean canHurt(final Player player, final Player target) {
        final Clan pClan = getManager().getClan(player.getUniqueId());
        final Clan tClan = getManager().getClan(target.getUniqueId());
        if (pClan != null && tClan != null) {
            return !pClan.equals(tClan) && !pClan.isAllied(tClan);
        }
        return true;
    }
}