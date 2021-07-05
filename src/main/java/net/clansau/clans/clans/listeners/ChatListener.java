package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.core.client.events.ClientChatEvent;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ChatListener extends CoreListener<ClanManager> {

    public ChatListener(final ClanManager manager) {
        super(manager, "Chat Listener");
    }

    @EventHandler
    public void onClientChat(final ClientChatEvent e) {
        e.setCancelled(true);
        final Player player = e.getPlayer();
        final Clan clan = getManager().getClan(player.getUniqueId());
        final String rank = e.getRankPrefix();
        for (final Player target : Bukkit.getOnlinePlayers()) {
            if (clan == null) {
                this.broadcastToEachPlayer(target, rank + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + ": " + e.getMessage());
                continue;
            }
            final ClanRelation relation = getManager().getClanRelation(getManager().getClan(target.getUniqueId()), clan);
            this.broadcastToEachPlayer(target, rank + relation.getPrefix() + clan.getName() + " " + relation.getSuffix() + player.getName() + ChatColor.WHITE + ": " + e.getMessage());
        }
    }

    private void broadcastToEachPlayer(final Player target, final String message) {
        UtilMessage.message(target, message);
    }
}