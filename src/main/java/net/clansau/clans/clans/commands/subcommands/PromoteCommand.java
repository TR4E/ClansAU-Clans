package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.MemberPromoteEvent;
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

public class PromoteCommand extends IClanCommand implements Listener {

    public PromoteCommand(final ClanManager manager) {
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
            UtilMessage.message(player, "Clans", "You did not input a Member to Promote.");
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
        if (!(this.canPromoteMember(player, client, target, clan))) {
            return;
        }
        if (clan.getClanRole(target.getUUID()).equals(ClanRole.LEADER)) {
            UtilMessage.message(player, "Clans", ChatColor.AQUA + target.getName() + ChatColor.GRAY + " cannot be promoted any further.");
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new MemberPromoteEvent(player, client, target, clan));
    }

    private boolean canPromoteMember(final Player player, final Client client, final Client target, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to promote a Member.");
                return false;
            }
            if (target.equals(client)) {
                UtilMessage.message(player, "Clans", "You cannot promote yourself.");
                return false;
            }
            if (target.isAdministrating() || clan.getClanRole(target.getUUID()).ordinal() >= clan.getClanRole(client.getUUID()).ordinal()) {
                UtilMessage.message(player, "Clans", "You do not outrank " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + ".");
                return false;
            }
        }
        return true;
    }

    private void promoteMember(final Player player, final Client client, final Client target, final Clan clan) {
        if (!(client.isAdministrating()) && clan.getClanRole(player.getUniqueId()).equals(ClanRole.LEADER) && clan.getClanRole(target.getUUID()).equals(ClanRole.ADMIN)) {
            clan.setClanRole(player.getUniqueId(), ClanRole.ADMIN);
        }
        clan.setClanRole(target.getUUID(), ClanRole.getClanRole(clan.getClanRole(target.getUUID()).ordinal() + 1));
        getManager().getRepository().updateMembers(clan);
        UtilMessage.message(player, "Clans", "You promoted " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + " to " + ChatColor.GREEN + UtilFormat.cleanString(clan.getClanRole(target.getUUID()).name()) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " promoted " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + " to " + ChatColor.GREEN + UtilFormat.cleanString(clan.getClanRole(target.getUUID()).name()) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId(), target.getUUID()});
        final Player targetPlayer = Bukkit.getPlayer(target.getUUID());
        if (targetPlayer != null && !(target.equals(client))) {
            UtilMessage.message(targetPlayer, "Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " promoted you to " + ChatColor.GREEN + UtilFormat.cleanString(clan.getClanRole(target.getUUID()).name()) + ChatColor.GRAY + ".");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMemberPromote(final MemberPromoteEvent e) {
        if (e.isCancelled()) {
            return;
        }
        this.promoteMember(e.getPlayer(), e.getClient(), e.getTarget(), e.getClan());
    }
}