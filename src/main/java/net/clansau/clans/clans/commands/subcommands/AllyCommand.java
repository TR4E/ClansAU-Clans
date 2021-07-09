package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.ClanAllyEvent;
import net.clansau.clans.config.OptionsManager;
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

public class AllyCommand extends IClanCommand implements Listener {

    public AllyCommand(final ClanManager manager) {
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Ally.");
            return;
        }
        final Clan target = getManager().searchClan(player, args[1], true);
        if (target == null) {
            return;
        }
        if (target.equals(clan)) {
            UtilMessage.message(player, "Clans", "You cannot request an alliance with yourself.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(this.canAllyClan(player, client, clan, target))) {
            return;
        }
        if (target.isAllied(clan)) {
            UtilMessage.message(player, "Clans", "You are already allies with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".");
            return;
        }
        if (client.isAdministrating()) {
            Bukkit.getServer().getPluginManager().callEvent(new ClanAllyEvent(player, clan, target, true));
            return;
        }
        if (clan.getAllianceReqMap().containsKey(target.getName()) && clan.getAllianceReqMap().get(target.getName()) > System.currentTimeMillis()) {
            UtilMessage.message(player, "Clans", "You have already requested an alliance with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".");
            return;
        }
        if (target.getAllianceReqMap().containsKey(clan.getName())) {
            Bukkit.getServer().getPluginManager().callEvent(new ClanAllyEvent(player, clan, target, false));
            return;
        }
        this.requestAlliance(player, clan, target);
    }

    private void acceptAlliance(final Player player, final Clan clan, final Clan target) {
        target.getAllianceReqMap().remove(clan.getName());
        clan.getAllianceReqMap().remove(target.getName());
        if (target.isEnemied(clan)) {
            clan.getEnemiesMap().remove(target.getName());
            target.getEnemiesMap().remove(clan.getName());
            getManager().getRepository().updateEnemies(clan);
            getManager().getRepository().updateEnemies(target);
        }
        clan.getAlliesMap().put(target.getName(), false);
        target.getAlliesMap().put(clan.getName(), false);
        getManager().getRepository().updateAllies(clan);
        getManager().getRepository().updateAllies(target);
        UtilMessage.message(player, "Clans", "You have accepted alliance with " + ChatColor.GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " accepted alliance with " + ChatColor.GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
        target.messageClan("Clans", ChatColor.GREEN + getManager().getName(clan, true) + ChatColor.GRAY + " has accepted alliance with your Clan.", null);
    }

    private void requestAlliance(final Player player, final Clan clan, final Clan target) {
        clan.getAllianceReqMap().put(target.getName(), System.currentTimeMillis() + 300000L);
        UtilMessage.message(player, "Clans", "You requested an alliance with " + ChatColor.YELLOW + getManager().getName(target, true) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " has requested an alliance with " + ChatColor.YELLOW + getManager().getName(target, true) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
        target.messageClan("Clans", ChatColor.YELLOW + getManager().getName(clan, true) + ChatColor.GRAY + " has requested an alliance with your Clan.", null);
    }

    private void forceAlliance(final Clan clan, final Clan target) {
        target.getAllianceReqMap().remove(clan.getName());
        clan.getAllianceReqMap().remove(target.getName());
        if (target.isEnemied(clan)) {
            clan.getEnemiesMap().remove(target.getName());
            target.getEnemiesMap().remove(clan.getName());
            getManager().getRepository().updateEnemies(clan);
            getManager().getRepository().updateEnemies(target);
        } else if (target.isPillaging(clan) || clan.isPillaging(target)) {
            clan.getPillagingMap().remove(target.getName());
            target.getPillagingMap().remove(clan.getName());
        }
        clan.getAlliesMap().put(target.getName(), false);
        target.getAlliesMap().put(clan.getName(), false);
        getManager().getRepository().updateAllies(clan);
        getManager().getRepository().updateAllies(target);
        clan.messageClan("Clans", "You are now allies with " + ChatColor.GREEN + getManager().getName(target, true) + ChatColor.GRAY + ".", null);
        target.messageClan("Clans", "You are now allies with " + ChatColor.GREEN + getManager().getName(clan, true) + ChatColor.GRAY + ".", null);
    }

    private boolean canAllyClan(final Player player, final Client client, final Clan clan, final Clan target) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to ally a Clan.");
                return false;
            }
            if (target instanceof AdminClan) {
                UtilMessage.message(player, "Clans", "You cannot request an alliance with Admin Clans.");
                return false;
            }
            if (target.isEnemied(clan) || target.isPillaging(clan) || clan.isPillaging(target)) {
                UtilMessage.message(player, "Clans", "You must be neutral with " + getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + ".");
                return false;
            }
            if (clan.getMembersMap().size() + clan.getAlliesMap().size() >= getInstance().getManager(OptionsManager.class).getClansMaxMembers()) {
                UtilMessage.message(player, "Clans", "Your Clan has too many members/allies.");
                return false;
            }
            if (target.getMembersMap().size() + target.getAlliesMap().size() >= getInstance().getManager(OptionsManager.class).getClansMaxMembers()) {
                UtilMessage.message(player, "Clans", getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, true) + ChatColor.GRAY + " has too many members/allies.");
                return false;
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanAlly(final ClanAllyEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.isForced()) {
            this.forceAlliance(e.getClan(), e.getTarget());
        } else {
            this.acceptAlliance(e.getPlayer(), e.getClan(), e.getTarget());
        }
    }
}