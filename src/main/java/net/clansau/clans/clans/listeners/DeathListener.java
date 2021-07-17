package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.general.combat.events.CustomDeathMessageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class DeathListener extends CoreListener<ClanManager> {

    public DeathListener(final ClanManager manager) {
        super(manager, "Death Listener");
    }

    @EventHandler
    public void onDeathMessage(final CustomDeathMessageEvent e) {
        e.setPlayerName(this.getName(e.getPlayer(), e.getTarget()));
        if (e.getKiller() != null) {
            e.setKillerName(this.getName(e.getKiller(), e.getTarget()));
        }
    }

    private String getName(final Player player, final Player target) {
        final Clan pClan = getManager().getClan(player.getUniqueId());
        final Clan tClan = getManager().getClan(target.getUniqueId());
        return getManager().getClanRelation(tClan, pClan).getSuffix() + player.getName();
    }
}