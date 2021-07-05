package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.core.client.events.ClientChatEvent;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ChatListener extends CoreListener<ClanManager> {

    public ChatListener(final ClanManager manager) {
        super(manager, "Chat Listener");
    }

    @EventHandler
    public void onClientChat(final ClientChatEvent e) {
        final Player player = e.getPlayer();
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            return;
        }
        e.setCancelled(true);
        final Player target = e.getTarget();
        final ClanRelation relation = getManager().getClanRelation(getManager().getClan(target.getUniqueId()), clan);
        UtilMessage.message(e.getTarget(), e.getRankPrefix() + relation.getPrefix() + clan.getName() + " " + relation.getSuffix() + player.getName() + ChatColor.WHITE + ": " + e.getMessage());
    }
}