package net.clansau.clans.clans.commands.chat;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.events.AllyChatEvent;
import net.clansau.core.client.Rank;
import net.clansau.core.framework.command.Command;
import net.clansau.core.gamer.Gamer;
import net.clansau.core.gamer.GamerManager;
import net.clansau.core.gamer.enums.ChatType;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AllyChatCommand extends Command<ClanManager, Player> implements Listener {

    public AllyChatCommand(final ClanManager manager) {
        super(manager, Player.class, "allychat", new String[]{"ac"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }
        if (args == null || args.length == 0) {
            final Gamer gamer = getInstance().getManager(GamerManager.class).getGamer(player.getUniqueId());
            if (gamer == null) {
                return;
            }
            if (gamer.getChatType().equals(ChatType.ALLY_CHAT)) {
                gamer.setChatType(ChatType.GLOBAL_CHAT);
                UtilMessage.message(player, "Clans", "Ally Chat: " + ChatColor.RED + "Disabled");
            } else {
                gamer.setChatType(ChatType.ALLY_CHAT);
                UtilMessage.message(player, "Clans", "Ally Chat: " + ChatColor.GREEN + "Enabled");
            }
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new AllyChatEvent(player, clan, UtilFormat.getFinalArg(args, 0)));
    }

    @Override
    protected void help(final Player player) {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAllyChat(final AllyChatEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        final Clan clan = e.getClan();
        final String message = e.getMessage();
        clan.messageClan(null, ChatColor.DARK_GREEN + clan.getName() + " " + player.getName() + ChatColor.GREEN + " " + message, null);
        clan.messageAllies(null, ChatColor.DARK_GREEN + clan.getName() + " " + player.getName() + ChatColor.GREEN + " " + message, null);
    }
}