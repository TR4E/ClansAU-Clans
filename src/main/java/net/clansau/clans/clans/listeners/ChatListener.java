package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.core.client.events.CustomChatEvent;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ChatListener extends CoreListener<ClanManager> {

    public ChatListener(final ClanManager manager) {
        super(manager, "Chat Listener");
    }

    @EventHandler
    public void onCustomChat(final CustomChatEvent e) {
        final Player player = e.getPlayer();
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            return;
        }
        final Player target = e.getTarget();
        final String message = e.getMessage();
        final ClanRelation relation = getManager().getClanRelation(getManager().getClan(target.getUniqueId()), clan);
        e.setFormat(e.getRankPrefix() + relation.getPrefix() + clan.getName() + " " + relation.getSuffix() + player.getName() + ChatColor.WHITE + ": " + message);
    }
}