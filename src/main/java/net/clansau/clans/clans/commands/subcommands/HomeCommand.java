package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.events.ClanHomeEvent;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.client.Rank;
import net.clansau.core.framework.recharge.RechargeManager;
import net.clansau.core.teleport.Teleport;
import net.clansau.core.teleport.TeleportManager;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class HomeCommand extends IClanCommand implements Listener {

    public HomeCommand(final ClanManager manager) {
        super(manager);
    }

    @Override
    protected void run(final Player player, final String[] args) {
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (args.length == 1) {
            if (clan == null) {
                UtilMessage.message(player, "Clans", "You are not in a Clan.");
                return;
            }
            final Location home = clan.getHome();
            if (home == null) {
                UtilMessage.message(player, "Clans", "Your Clan has not set a Home.");
                return;
            }
            if (!(this.canTeleportHome(player, client))) {
                return;
            }
            Bukkit.getServer().getPluginManager().callEvent(new ClanHomeEvent(player, clan, home));
            return;
        }
        if (args.length == 2) {
            if (!(client.hasRank(Rank.ADMIN, true))) {
                return;
            }
            if (!(client.isAdministrating())) {
                UtilMessage.message(player, "Clans", "You must be Administrating to teleport to a Clan Home.");
                return;
            }
            final Clan target = getManager().searchClan(player, args[1], true);
            if (target == null) {
                return;
            }
            if (target.getHome() == null) {
                UtilMessage.message(player, "Clans", getManager().getClanRelation(clan, target).getSuffix() + getManager().getName(target, !(target instanceof AdminClan)) + ChatColor.GRAY + " has not set a Clan Home.");
                return;
            }
            Bukkit.getServer().getPluginManager().callEvent(new ClanHomeEvent(player, target, target.getHome()));
        }
    }

    private boolean canTeleportHome(final Player player, final Client client) {
        if (!(client.isAdministrating())) {
            final Clan land = getManager().getClan(player.getLocation());
            if (land == null || !(land instanceof AdminClan && getManager().isSafe(player.getLocation()) && land.getName().toLowerCase().contains("spawn"))) {
                UtilMessage.message(player, "Clans", "You can only use Clan Home from " + ChatColor.WHITE + "Spawn" + ChatColor.GRAY + ".");
                return false;
            }
            return getInstance().getManager(RechargeManager.class).add(player, "Clan Home", 30000L, true);
        }
        return true;
    }

    private void teleportHome(final Player player, final Clan clan, final Location home) {
        final Teleport teleport = new Teleport(player.getUniqueId(), home, 0L, false, true);
        getInstance().getManager(TeleportManager.class).addTeleport(teleport);
        if (clan.isMember(player.getUniqueId())) {
            UtilMessage.message(player, "Clans", "You teleported to Clan Home.");
        } else {
            UtilMessage.message(player, "Clans", "You teleported to the Clan Home of " + getManager().getClanRelation(getManager().getClan(player.getUniqueId()), clan).getSuffix() + getManager().getName(clan, !(clan instanceof AdminClan)) + ChatColor.GRAY + ".");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanHome(final ClanHomeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        this.teleportHome(e.getPlayer(), e.getClan(), e.getHome());
    }
}