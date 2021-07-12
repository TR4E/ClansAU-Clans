package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.enums.ClanRelation;
import net.clansau.clans.clans.events.AllyChatEvent;
import net.clansau.clans.clans.events.ClanChatEvent;
import net.clansau.core.client.events.CustomChatEvent;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.gamer.Gamer;
import net.clansau.core.gamer.GamerManager;
import net.clansau.core.gamer.enums.ChatType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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

    @EventHandler
    public void onClanChat(final AsyncPlayerChatEvent e) {
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
        if (gamer.getChatType().equals(ChatType.CLAN_CHAT)) {
            e.setCancelled(true);
            Bukkit.getServer().getPluginManager().callEvent(new ClanChatEvent(player, clan, e.getMessage()));
        } else if (gamer.getChatType().equals(ChatType.ALLY_CHAT)) {
            e.setCancelled(true);
            Bukkit.getServer().getPluginManager().callEvent(new AllyChatEvent(player, clan, e.getMessage()));
        }
    }
}