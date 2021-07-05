package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class KickCommand extends IClanCommand {

    public KickCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }
        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Member to Kick.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Client target = getManager().searchMember(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.equals(client)) {
            UtilMessage.message(player, "Clans", "You cannot kick yourself.");
            return;
        }
        if (!(this.canKickMember(player, client, target, clan))) {
            return;
        }
        clan.getMembersMap().remove(target.getUUID());
        getManager().getRepository().updateMembers(clan);
        getManager().removeClanChat(target.getUUID());
        UtilMessage.message(player, "Clans", "You kicked " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " from the Clan.");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " kicked " + ChatColor.YELLOW + target.getName() + ChatColor.GRAY + " from the Clan.", new UUID[]{player.getUniqueId()});
        final Player targetPlayer = Bukkit.getPlayer(target.getUUID());
        if (targetPlayer != null && !(target.equals(client))) {
            UtilMessage.message(targetPlayer, "Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " kicked you from " + ChatColor.YELLOW + getManager().getName(clan, true) + ChatColor.GRAY + ".");
        }
    }

    private boolean canKickMember(final Player player, final Client client, final Client target, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to kick a Member.");
                return false;
            }
            if (target.isAdministrating() || clan.getClanRole(target.getUUID()).ordinal() >= clan.getClanRole(client.getUUID()).ordinal()) {
                UtilMessage.message(player, "Clans", "You do not outrank " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + ".");
            }
            if (clan.isBeingPillaged()) {
                UtilMessage.message(player, "Clans", "You cannot kick a member while being conquered by another Clan.");
                return false;
            }
            final Player targetPlayer = Bukkit.getPlayer(target.getUUID());
            if (targetPlayer != null) {
                final Clan land = getManager().getClan(targetPlayer.getLocation());
                if (land != null && clan.isEnemied(land)) {
                    UtilMessage.message(player, "Clans", "You cannot kick a member who is in enemy territory.");
                    return false;
                }
            }
        }
        return true;
    }
}