package net.clansau.clans.clans.commands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.subcommands.*;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.client.Rank;
import net.clansau.core.framework.Module;
import net.clansau.core.framework.command.Command;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClanCommand extends Command<ClanManager, Player> {

    public ClanCommand(final ClanManager manager) {
        super(manager, Player.class, "clan", new String[]{"c", "f", "fac", "faction", "gang", "g"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args == null || args.length == 0) {
            this.clanCommand(player, args);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "help": {
                help(player);
                break;
            }
            case "list": {
                getCommand(ListCommand.class).execute(player, args);
                break;
            }
            case "top": {
                getCommand(TopCommand.class).execute(player, args);
                break;
            }
            case "create": {
                getCommand(CreateCommand.class).execute(player, args);
                break;
            }
            case "disband": {
                getCommand(DisbandCommand.class).execute(player, args);
                break;
            }
            case "invite": {
                getCommand(InviteCommand.class).execute(player, args);
                break;
            }
            case "join": {
                getCommand(JoinCommand.class).execute(player, args);
                break;
            }
            case "leave": {
                getCommand(LeaveCommand.class).execute(player, args);
                break;
            }
            case "kick": {
                getCommand(KickCommand.class).execute(player, args);
                break;
            }
            case "promote": {
                getCommand(PromoteCommand.class).execute(player, args);
                break;
            }
            case "demote": {
                getCommand(DemoteCommand.class).execute(player, args);
                break;
            }
            case "neutral": {
                getCommand(NeutralCommand.class).execute(player, args);
                break;
            }
            case "ally": {
                getCommand(AllyCommand.class).execute(player, args);
                break;
            }
            case "trust": {
                getCommand(TrustCommand.class).execute(player, args);
                break;
            }
            case "enemy": {
                getCommand(EnemyCommand.class).execute(player, args);
                break;
            }
            default: {
                this.clanCommand(player, args);
                break;
            }
        }
    }

    @Override
    protected void help(final Player player) {
        UtilMessage.message(player, ChatColor.YELLOW + "----- Clans Help -----");
        UtilMessage.messageCMD(player, "/c create <name>", "Create a Clan.");
        UtilMessage.messageCMD(player, "/c invite <player>", "Invite a Player to your Clan.");
        UtilMessage.messageCMD(player, "/c join <clan>", "Join a Clan.");
        UtilMessage.messageCMD(player, "/c promote <player>", "Promote a Player in your Clan.");
        UtilMessage.messageCMD(player, "/c demote <player>", "Demote a Player in your Clan.");
        UtilMessage.messageCMD(player, "/c kick <player>", "Kick a Player from your Clan (admin).");
        UtilMessage.messageCMD(player, "/c claim", "Claim a chunk of land for your Clan (admin).");
        UtilMessage.messageCMD(player, "/c unclaim", "Un-claim a chunk of land for your Clan (admin).");
        UtilMessage.messageCMD(player, "/c sethome", "Set your Clan Home (admin).");
        UtilMessage.messageCMD(player, "/c home", "Teleport to your Clan Home.");
        UtilMessage.messageCMD(player, "/c energy", "Buy Clan Energy here.");
        UtilMessage.messageCMD(player, "/c leave", "Leave your Clan.");
        UtilMessage.messageCMD(player, "/c disband", "Disband your Clan, kicking all members and losing all land (leader).");
        UtilMessage.messageCMD(player, "/c enemy <clan>", "Wage war against another Clan (admin).");
        UtilMessage.messageCMD(player, "/c neutral <clan>", "Request neutrality between your clan and another (admin).");
        UtilMessage.messageCMD(player, "/c ally <clan>", "Request an alliance with another Clan (admin).");
        UtilMessage.messageCMD(player, "/c trust <clan>", "Give another clan access to your claims (admin).");
        UtilMessage.messageCMD(player, "/c map", "Show a map of all clan claims around you.");
        UtilMessage.messageCMD(player, "/cc", "Toggle Clan Chat.");
        UtilMessage.messageCMD(player, "/ac", "Toggle Ally Chat.");
    }

    private void clanCommand(final Player player, final String[] args) {
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (args == null || args.length == 0) {
            if (clan == null) {
                UtilMessage.message(player, "Clans", "You are not in a Clan.");
                return;
            }
            UtilMessage.message(player, "Clans", ChatColor.AQUA + clan.getName() + ChatColor.GRAY + " Information:");
            for (final String msg : this.getClanInformation(client, clan)) {
                UtilMessage.message(player, msg);
            }
            return;
        }
        final Clan target = getManager().searchClan(player, args[0], true);
        if (target == null) {
            return;
        }
        if (target instanceof AdminClan) {
            if (!(target.isMember(player.getUniqueId()) || client.isAdministrating())) {
                UtilMessage.message(player, "Clans", "You cannot view Admin Clans.");
                return;
            }
        }
        UtilMessage.message(player, "Clans", getManager().getClanRelation(clan, target).getSuffix() + target.getName() + ChatColor.GRAY + " Information:");
        for (final String msg : this.getClanInformation(client, target)) {
            UtilMessage.message(player, msg);
        }
    }

    private List<String> getClanInformation(final Client client, final Clan clan) {
        final Clan pClan = getManager().getClan(client.getUUID());
        final List<String> list = new ArrayList<>();
        if (client.isAdministrating()) {
            list.add(ChatColor.WHITE + "Founder: " + ChatColor.YELLOW + getInstance().getManager(ClientManager.class).tryGetOnlineClient(clan.getFounder()).getName());
        }
        list.add(ChatColor.WHITE + "Age: " + ChatColor.YELLOW + clan.getAge());
        list.add(ChatColor.WHITE + "Territory: " + ChatColor.YELLOW + clan.getTerritoryString());
        if (client.isAdministrating() || clan.isMember(client.getUUID())) {
            list.add(ChatColor.WHITE + "Home: " + ChatColor.YELLOW + clan.getHomeString());
        }
        list.add(ChatColor.WHITE + "Allies: " + clan.getAlliesString(pClan));
        list.add(ChatColor.WHITE + "Enemies: " + clan.getEnemiesString(pClan));
        list.add(ChatColor.WHITE + "Members: " + clan.getMembersString());
        list.add(ChatColor.WHITE + "TNT Protection: " + clan.getTNTProtectionString());
//        list.add(ChatColor.WHITE + "Energy: " + clan.getEnergyString());
        list.add(ChatColor.WHITE + "Points: " + ChatColor.RED + ChatColor.BOLD + clan.getPoints());
        return list;
    }

    private <T extends Module<?>> T getCommand(final Class<T> clazz) {
        return getManager().getModule(clazz);
    }
}