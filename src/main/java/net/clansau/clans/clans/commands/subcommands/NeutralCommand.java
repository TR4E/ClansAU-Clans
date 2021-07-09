package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.ClanNeutralEvent;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class NeutralCommand extends IClanCommand implements Listener {

    public NeutralCommand(final ClanManager manager) {
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Neutral.");
            return;
        }
        final Clan target = getManager().searchClan(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.equals(clan)) {
            UtilMessage.message(player, "Clans", "You cannot request an neutrality with yourself.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(this.canNeutralClan(player, client, clan, target))) {
            return;
        }
        if (target.isNeutraled(clan)) {
            UtilMessage.message(player, "Clans", "You are already neutral with " + ChatColor.YELLOW + getManager().getName(target, true) + ChatColor.GRAY + ".");
            return;
        }
        if (target.isAllied(clan) || client.isAdministrating()) {
            Bukkit.getServer().getPluginManager().callEvent(new ClanNeutralEvent(player, clan, target, true));
            return;
        }
        if (clan.getNeutralReqMap().containsKey(target.getName()) && clan.getNeutralReqMap().get(target.getName()) > System.currentTimeMillis()) {
            UtilMessage.message(player, "Clans", "You have already requested to neutral with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".");
            return;
        }
        if (target.getNeutralReqMap().containsKey(clan.getName())) {
            Bukkit.getServer().getPluginManager().callEvent(new ClanNeutralEvent(player, clan, target, false));
            return;
        }
        this.requestNeutral(player, clan, target);
    }

    private void acceptNeutral(final Player player, final Clan clan, final Clan target) {
        target.getNeutralReqMap().remove(clan.getName());
        clan.getNeutralReqMap().remove(target.getName());
        if (target.isEnemied(clan)) {
            clan.getEnemiesMap().remove(target.getName());
            target.getEnemiesMap().remove(clan.getName());
            getManager().getRepository().updateEnemies(clan);
            getManager().getRepository().updateEnemies(target);
        } else if (target.isAllied(clan)) {
            clan.getAlliesMap().remove(target.getName());
            target.getAlliesMap().remove(clan.getName());
            getManager().getRepository().updateAllies(clan);
            getManager().getRepository().updateAllies(target);
        }
        UtilMessage.message(player, "Clans", "You have accepted neutrality with " + ChatColor.YELLOW + getManager().getName(target, true) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " accepted neutrality with " + ChatColor.YELLOW + getManager().getName(target, true) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
        target.messageClan("Clans", ChatColor.YELLOW + getManager().getName(clan, true) + ChatColor.GRAY + " has accepted neutrality with your Clan.", null);
    }

    private void requestNeutral(final Player player, final Clan clan, final Clan target) {
        clan.getNeutralReqMap().put(target.getName(), System.currentTimeMillis() + 300000L);
        UtilMessage.message(player, "Clans", "You requested neutrality with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " has requested neutrality with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
        target.messageClan("Clans", getManager().getClanRelation(target, clan).getSuffix() + getManager().getName(clan, true) + ChatColor.GRAY + " has requested neutrality with your Clan.", null);
    }

    private void forceNeutral(final Clan clan, final Clan target) {
        target.getNeutralReqMap().remove(clan.getName());
        clan.getNeutralReqMap().remove(target.getName());
        if (target.isEnemied(clan)) {
            clan.getEnemiesMap().remove(target.getName());
            target.getEnemiesMap().remove(clan.getName());
            getManager().getRepository().updateEnemies(clan);
            getManager().getRepository().updateEnemies(target);
        } else if (target.isAllied(clan)) {
            clan.getAlliesMap().remove(target.getName());
            target.getAlliesMap().remove(clan.getName());
            getManager().getRepository().updateAllies(clan);
            getManager().getRepository().updateAllies(target);
        } else if (target.isPillaging(clan) || clan.isPillaging(target)) {
            clan.getPillagingMap().remove(target.getName());
            target.getPillagingMap().remove(clan.getName());
        }
        clan.messageClan("Clans", "You are now neutral with " + ChatColor.YELLOW + getManager().getName(target, true) + ChatColor.GRAY + ".", null);
        target.messageClan("Clans", "You are now neutral with " + ChatColor.YELLOW + getManager().getName(clan, true) + ChatColor.GRAY + ".", null);
    }

    private boolean canNeutralClan(final Player player, final Client client, final Clan clan, final Clan target) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to neutral a Clan.");
                return false;
            }
            if (target instanceof AdminClan) {
                UtilMessage.message(player, "Clans", "You cannot request neutrality with Admin Clans.");
                return false;
            }
            if (target.isPillaging(clan) || clan.isPillaging(target)) {
                UtilMessage.message(player, "Clans", "You are not neutral with " + ChatColor.LIGHT_PURPLE + getManager().getName(target, true) + ChatColor.GRAY + " until the pillage is over.");
                return false;
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanNeutral(final ClanNeutralEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.isForced()) {
            this.forceNeutral(e.getClan(), e.getTarget());
        } else {
            this.acceptNeutral(e.getPlayer(), e.getClan(), e.getTarget());
        }
    }
}