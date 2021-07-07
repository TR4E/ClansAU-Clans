package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.clans.clans.events.AllyChatEvent;
import net.clansau.clans.clans.events.ClanChatEvent;
import net.clansau.core.client.events.ClientChatEvent;
import net.clansau.core.client.events.CustomChatEvent;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.gamer.Gamer;
import net.clansau.core.gamer.GamerManager;
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
    public void onCustomChat(final CustomChatEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            return;
        }
        final Gamer gamer = getInstance().getManager(GamerManager.class).getGamer(player.getUniqueId());
        if (gamer == null) {
            return;
        }
        switch (gamer.getChatType()) {
            case CLAN_CHAT: {
                e.setCancelled(true);
                Bukkit.getServer().getPluginManager().callEvent(new ClanChatEvent(player, clan, e.getMessage()));
                break;
            }
            case ALLY_CHAT: {
                e.setCancelled(true);
                Bukkit.getServer().getPluginManager().callEvent(new AllyChatEvent(player, clan, e.getMessage()));
                break;
            }
        }
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