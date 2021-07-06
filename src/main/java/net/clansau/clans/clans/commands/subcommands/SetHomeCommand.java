package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.ClanSetHomeEvent;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class SetHomeCommand extends IClanCommand implements Listener {

    public SetHomeCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }
        final Clan land = getManager().getClan(player.getLocation());
        if (land == null || !(land.equals(clan))) {
            UtilMessage.message(player, "Clans", "You must set your Clan Home in your own land.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (!(this.canSetHome(player, client, clan))) {
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new ClanSetHomeEvent(player, clan, player.getLocation()));
    }

    private boolean canSetHome(final Player player, final Client client, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to manage the Clan Home.");
                return false;
            }
        }
        return true;
    }

    private void setHome(final Player player, final Clan clan, final Location home) {
        clan.setHome(home);
        getManager().getRepository().updateHome(clan);
        UtilMessage.message(player, "Clans", "You set the Clan Home at " + clan.getHomeString() + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " set the Clan Home at " + clan.getHomeString() + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanSetHome(final ClanSetHomeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        this.setHome(e.getPlayer(), e.getClan(), e.getHome());
    }
}