package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.config.OptionsManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilMessage;
import net.clansau.core.utility.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InviteCommand extends IClanCommand {

    public InviteCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(this.canUseInvite(player, client, clan))) {
            return;
        }
        if (clan instanceof AdminClan) {
            UtilMessage.message(player, "Clans", "You cannot invite players to an Admin Clan.");
            return;
        }
        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Player to Invite.");
            return;
        }
        final Player target = UtilPlayer.searchPlayer(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.equals(player)) {
            UtilMessage.message(player, "Clans", "You cannot invite yourself.");
            return;
        }
        if (clan.isMember(target.getUniqueId())) {
            UtilMessage.message(player, "Clans", ChatColor.AQUA + target.getName() + ChatColor.GRAY + " is already apart of your Clan.");
            return;
        }
        final Clan tClan = getManager().getClan(target.getUniqueId());
        if (tClan != null) {
            UtilMessage.message(player, "Clans", getManager().getClanRelation(clan, tClan).getSuffix() + target.getName() + ChatColor.GRAY + " is apart of " + getManager().getClanRelation(clan, tClan).getSuffix() + getManager().getName(tClan, true) + ChatColor.GRAY + ".");
            return;
        }
        if (clan.getInviteeReqMap().containsKey(target.getUniqueId()) && clan.getInviteeReqMap().get(target.getUniqueId()) > System.currentTimeMillis()) {
            UtilMessage.message(player, "Clans", ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " has already been invited to your Clan.");
            return;
        }
        clan.getInviteeReqMap().put(target.getUniqueId(), System.currentTimeMillis() + 300000L);
        UtilMessage.message(player, "Clans", "You invited " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to your Clan.");
        UtilMessage.message(target, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has invited you to join " + ChatColor.YELLOW + getManager().getName(clan, true) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " has invited " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " to join the Clan.", new UUID[]{player.getUniqueId()});
    }

    private boolean canUseInvite(final Player player, final Client client, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to invite Player.");
                return false;
            }
            if (clan.getMembersMap().size() + clan.getAlliesMap().size() >= getInstance().getManager(OptionsManager.class).getClansMaxMembers()) {
                UtilMessage.message(player, "Clans", "Your Clan has too many members/allies.");
                return false;
            }
            if (clan.isBeingPillaged()) {
                UtilMessage.message(player, "Clans", "You cannot invite a Player while being conquered by another Clan.");
                return false;
            }
        }
        return true;
    }
}