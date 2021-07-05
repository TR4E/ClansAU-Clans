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

import java.util.UUID;

public class TrustCommand extends IClanCommand {

    public TrustCommand(final ClanManager manager) {
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Trust.");
            return;
        }
        final Clan target = getManager().searchClan(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.equals(clan)) {
            UtilMessage.message(player, "Clans", "You cannot request trust with yourself.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(this.canTrustClan(player, client, clan, target))) {
            return;
        }
        if (!(target.isAllied(clan))) {
            UtilMessage.message(player, "Clans", "You are not allies with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".");
            return;
        }
        if (client.isAdministrating() && target.isAllied(clan) && !(target.isTrusted(clan))) {
            this.forceTrust(clan, target);
            return;
        }
        if (target.isTrusted(clan)) {
            this.revokeTrust(player, clan, target);
            return;
        }
        if (clan.getTrustReqMap().containsKey(target.getName()) && clan.getTrustReqMap().get(target.getName()) > System.currentTimeMillis()) {
            UtilMessage.message(player, "Clans", "You have already requested to trust with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".");
            return;
        }
        if (target.getTrustReqMap().containsKey(clan.getName())) {
            this.acceptTrust(player, clan, target);
            return;
        }
        this.requestTrust(player, clan, target);
    }

    private void acceptTrust(final Player player, final Clan clan, final Clan target) {
        target.getTrustReqMap().remove(clan.getName());
        clan.getTrustReqMap().remove(target.getName());
        clan.getAlliesMap().put(target.getName(), true);
        target.getAlliesMap().put(clan.getName(), true);
        getManager().getRepository().updateAllies(clan);
        getManager().getRepository().updateAllies(target);
        UtilMessage.message(player, "Clans", "You have accepted trust with " + ChatColor.DARK_GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " accepted trust with " + ChatColor.DARK_GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
        target.messageClan("Clans", ChatColor.DARK_GREEN + getManager().getName(clan, true) + ChatColor.GRAY + " has accepted trust with your Clan.", null);
    }

    private void revokeTrust(final Player player, final Clan clan, final Clan target) {
        clan.getAlliesMap().put(target.getName(), false);
        target.getAlliesMap().put(clan.getName(), false);
        getManager().getRepository().updateAllies(clan);
        getManager().getRepository().updateAllies(target);
        UtilMessage.message(player, "Clans", "You have revoked trust with " + ChatColor.GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " has revoked trust with " + ChatColor.GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
        target.messageClan("Clans", ChatColor.GREEN + getManager().getName(clan, true) + ChatColor.GRAY + " has revoked trust with your Clan.", null);
    }

    private void requestTrust(final Player player, final Clan clan, final Clan target) {
        clan.getTrustReqMap().put(target.getName(), System.currentTimeMillis() + 300000L);
        UtilMessage.message(player, "Clans", "You requested to trust with " + ChatColor.GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " has requested to trust with " + ChatColor.GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
        target.messageClan("Clans", ChatColor.GREEN + getManager().getName(clan, true) + ChatColor.GRAY + " has requested to trust with your Clan.", null);
    }

    private void forceTrust(final Clan clan, final Clan target) {
        clan.getTrustReqMap().remove(target.getName());
        target.getTrustReqMap().remove(clan.getName());
        clan.getAlliesMap().put(target.getName(), true);
        target.getAlliesMap().put(clan.getName(), true);
        getManager().getRepository().updateAllies(clan);
        getManager().getRepository().updateAllies(target);
        clan.messageClan("Clans", "You are now trusted with " + ChatColor.DARK_GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".", null);
        target.messageClan("Clans", "You are now trusted with " + ChatColor.DARK_GREEN + getManager().getName(clan, true) + ChatColor.GRAY + ".", null);
    }

    private boolean canTrustClan(final Player player, final Client client, final Clan clan, final Clan target) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to trust a Clan.");
                return false;
            }
            if (target instanceof AdminClan) {
                UtilMessage.message(player, "Clans", "You cannot request trust with Admin Clans.");
                return false;
            }
            if (target.isEnemied(clan) || target.isBeingPillagedBy(clan) || clan.isBeingPillagedBy(target)) {
                UtilMessage.message(player, "Clans", "You must be neutral with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".");
                return false;
            }
        }
        return true;
    }
}