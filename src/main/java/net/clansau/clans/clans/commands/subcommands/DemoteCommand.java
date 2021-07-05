package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.MemberDemoteEvent;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class DemoteCommand extends IClanCommand implements Listener {

    public DemoteCommand(final ClanManager manager) {
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
            UtilMessage.message(player, "Clans", "You did not input a Member to Demote.");
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
        if (!(this.canDemoteMember(player, client, target, clan))) {
            return;
        }
        if (clan.getClanRole(target.getUUID()).equals(ClanRole.RECRUIT)) {
            UtilMessage.message(player, "Clans", ChatColor.AQUA + target.getName() + ChatColor.GRAY + " cannot be demoted any further.");
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new MemberDemoteEvent(player, client, target, clan));
    }

    private boolean canDemoteMember(final Player player, final Client client, final Client target, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to demote a Member.");
                return false;
            }
            if (target.equals(client)) {
                UtilMessage.message(player, "Clans", "You cannot demote yourself.");
                return false;
            }
            if (target.isAdministrating() || clan.getClanRole(target.getUUID()).ordinal() >= clan.getClanRole(client.getUUID()).ordinal()) {
                UtilMessage.message(player, "Clans", "You do not outrank " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + ".");
                return false;
            }
        }
        return true;
    }

    private void demoteMember(final Player player, final Client client, final Client target, final Clan clan) {
        clan.setClanRole(target.getUUID(), ClanRole.getClanRole(clan.getClanRole(target.getUUID()).ordinal() - 1));
        getManager().getRepository().updateMembers(clan);
        UtilMessage.message(player, "Clans", "You demoted " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + " to " + ChatColor.GREEN + UtilFormat.cleanString(clan.getClanRole(target.getUUID()).name()) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " demoted " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + " to " + ChatColor.GREEN + UtilFormat.cleanString(clan.getClanRole(target.getUUID()).name()) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId(), target.getUUID()});
        final Player targetPlayer = Bukkit.getPlayer(target.getUUID());
        if (targetPlayer != null && !(target.equals(client))) {
            UtilMessage.message(targetPlayer, "Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " demoted you to " + ChatColor.GREEN + UtilFormat.cleanString(clan.getClanRole(target.getUUID()).name()) + ChatColor.GRAY + ".");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMemberDemote(final MemberDemoteEvent e) {
        if (e.isCancelled()) {
            return;
        }
        this.demoteMember(e.getPlayer(), e.getClient(), e.getTarget(), e.getClan());
    }
}