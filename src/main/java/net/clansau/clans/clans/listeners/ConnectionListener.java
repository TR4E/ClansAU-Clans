package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener extends CoreListener<ClanManager> {

    public ConnectionListener(final ClanManager manager) {
        super(manager, "Connection Listener");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMemberQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            return;
        }
        if (clan.getOnlineMembers().size() > 1) {
            return;
        }
        clan.setLastOnline(System.currentTimeMillis());
        getManager().getRepository().updateLastOnline(clan);
    }
}