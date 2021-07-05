package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.ClanJoinEvent;
import net.clansau.clans.config.OptionsManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.recharge.RechargeManager;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class JoinCommand extends IClanCommand implements Listener {

    public JoinCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        if (getManager().getClan(player.getUniqueId()) != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan.");
            return;
        }
        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Clan to Join.");
            return;
        }
        final Clan clan = getManager().searchClan(player, args[1], true);
        if (clan == null) {
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(this.canJoinClan(player, client, clan))) {
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new ClanJoinEvent(player, clan, client.isAdministrating()));
    }

    private boolean canJoinClan(final Player player, final Client client, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (clan instanceof AdminClan) {
                UtilMessage.message(player, "Clans", "You cannot join Admin Clans.");
                return false;
            }
            if (clan.getMembersMap().size() + clan.getAlliesMap().size() >= getInstance().getManager(OptionsManager.class).getClansMaxMembers()) {
                UtilMessage.message(player, "Clans", ChatColor.YELLOW + getManager().getName(clan, true) + ChatColor.GRAY + " has too many members/allies.");
                return false;
            }
            if (!(clan.getInviteeReqMap().containsKey(player.getUniqueId()) && clan.getInviteeReqMap().get(player.getUniqueId()) > System.currentTimeMillis())) {
                UtilMessage.message(player, "Clans", "You have not been invited to " + ChatColor.YELLOW + getManager().getName(clan, true) + ChatColor.GRAY + ".");
                if (getInstance().getManager(RechargeManager.class).add(player, "JoinAttempt " + clan.getName(), 150000L, false)) {
                    clan.messageClan("Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " tried to join your Clan.", null);
                }
                return false;
            }
        }
        return true;
    }

    private void joinClan(final Player player, final Clan clan, final boolean administrating) {
        if (administrating) {
            clan.getMembersMap().put(player.getUniqueId(), ClanRole.LEADER);
        } else {
            clan.getMembersMap().put(player.getUniqueId(), ClanRole.RECRUIT);
        }
        getManager().getRepository().updateMembers(clan);
        clan.getInviteeReqMap().remove(player.getUniqueId());
        UtilMessage.message(player, "Clans", "You joined " + ChatColor.AQUA + getManager().getName(clan, !(clan instanceof AdminClan)) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " joined the Clan.", new UUID[]{player.getUniqueId()});
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanJoin(final ClanJoinEvent e) {
        if (e.isCancelled()) {
            return;
        }
        this.joinClan(e.getPlayer(), e.getClan(), e.isForced());
    }
}