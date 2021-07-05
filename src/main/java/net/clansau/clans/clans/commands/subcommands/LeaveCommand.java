package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LeaveCommand extends IClanCommand {

    public LeaveCommand(final ClanManager manager) {
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
        if (!(this.canLeaveClan(player, client, clan))) {
            return;
        }
        if (!(client.isAdministrating())) {
            if (clan.getMembersMap().size() <= 1 && !(clan instanceof AdminClan)) {
                getManager().getModule(DisbandCommand.class).execute(player, args);
                return;
            }
        }
        clan.getMembersMap().remove(player.getUniqueId());
        getManager().getRepository().updateMembers(clan);
        getManager().removeClanChat(player.getUniqueId());
        UtilMessage.message(player, "Clans", "You left " + ChatColor.YELLOW + getManager().getName(clan, !(clan instanceof AdminClan)) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " left the Clan.", null);
    }

    private boolean canLeaveClan(final Player player, final Client client, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (clan.getMembersMap().size() > 1 && clan.hasClanRole(player.getUniqueId(), ClanRole.LEADER)) {
                UtilMessage.message(player, "Clans", "You must pass on Leadership before leaving.");
                return false;
            }
            if (clan.isBeingPillaged()) {
                UtilMessage.message(player, "Clans", "You cannot leave your Clan while being conquered by another Clan.");
                return false;
            }
            final Clan land = getManager().getClan(player.getLocation());
            if (land != null && clan.isEnemied(land)) {
                UtilMessage.message(player, "Clans", "You cannot leave your Clan while in enemy territory.");
                return false;
            }
        }
        return true;
    }
}