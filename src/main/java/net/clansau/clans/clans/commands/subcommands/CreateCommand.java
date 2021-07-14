package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.ClanModule;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.ClanCreateEvent;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.client.Rank;
import net.clansau.core.framework.recharge.RechargeManager;
import net.clansau.core.scoreboard.events.ScoreboardUpdateEvent;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CreateCommand extends IClanCommand implements Listener {

    public CreateCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    public void run(final Player player, final String[] args) {
        if (getManager().getClan(player.getUniqueId()) != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan.");
            return;
        }
        if (args.length == 1) {
            UtilMessage.message(player, "Clans", "You did not input a Clan name.");
            return;
        }
        final String name = args[1];
        if (getManager().isClan(name)) {
            UtilMessage.message(player, "Clans", "Clan name is already used by another Clan.");
            return;
        }
        if (UtilFormat.hasSymbols(name)) {
            UtilMessage.message(player, "Clans", "You cannot have special characters in your Clan name.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(this.canCreateClan(player, client, name))) {
            return;
        }
        Clan clan = new Clan(getInstance(), name);
        if (client.hasRank(Rank.OWNER, false) && client.isAdministrating()) {
            clan = new AdminClan(getInstance(), name);
        }
        Bukkit.getServer().getPluginManager().callEvent(new ClanCreateEvent(player, clan));
    }

    private boolean canCreateClan(final Player player, final Client client, final String name) {
        if (!(client.isAdministrating())) {
            if (!(getManager().isNameAllowed(name))) {
                UtilMessage.message(player, "Clans", "You cannot use that as your Clan name.");
                return false;
            }
            final int maxNameLength = getManager().getModule(ClanModule.class).getPrimitiveCasted(Integer.class, "MaxNameLength");
            if (name.length() > maxNameLength) {
                UtilMessage.message(player, "Clans", "Clan name is too long. Maximum length is " + ChatColor.YELLOW + maxNameLength + ChatColor.GRAY + ".");
                return false;
            }
            final int minNameLength = getManager().getModule(ClanModule.class).getPrimitiveCasted(Integer.class, "MinNameLength");
            if (name.length() < minNameLength) {
                UtilMessage.message(player, "Clans", "Clan name is too short. Minimum length is " + ChatColor.YELLOW + minNameLength + ChatColor.GRAY + ".");
                return false;
            }
            return !(getInstance().getManager(RechargeManager.class).isCooling(player, "Clan Create", true));
        }
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanCreate(final ClanCreateEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        if (!(getInstance().getManager(RechargeManager.class).add(player, "Create Clan", 300000L, true))) {
            return;
        }
        final Clan clan = e.getClan();
        clan.setFounder(player.getUniqueId());
        clan.getMembersMap().put(player.getUniqueId(), ClanRole.LEADER);
        getManager().addClan(clan);
        getManager().getRepository().saveClan(clan);
        UtilMessage.broadcast("Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " formed " + ChatColor.YELLOW + (clan instanceof AdminClan ? "Admin Clan " : "Clan ") + clan.getName() + ChatColor.GRAY + ".", null);
        Bukkit.getServer().getPluginManager().callEvent(new ScoreboardUpdateEvent(player));
    }
}