package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.ClanDisbandEvent;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.updater.UpdateEvent;
import net.clansau.core.framework.updater.Updater;
import net.clansau.core.scoreboard.events.ScoreboardUpdateEvent;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.WeakHashMap;

public class DisbandCommand extends IClanCommand implements Listener {

    private final Map<String, Long> confirmation;

    public DisbandCommand(final ClanManager manager) {
        super(manager);
        this.confirmation = new WeakHashMap<>();
    }

    public final Map<String, Long> getConfirmation() {
        return this.confirmation;
    }

    @Override
    protected void run(final Player player, final String[] args) {
        final Clan clan = getManager().getClan(player.getUniqueId());
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (clan instanceof AdminClan && !(client.isAdministrating())) {
            UtilMessage.message(player, "Clans", "You must be Administrating to disband Admin Clans.");
            return;
        }
        if (!(this.canDisbandClan(player, client, clan))) {
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new ClanDisbandEvent(player, clan, ClanDisbandEvent.DisbandCause.PLAYER));
    }

    private boolean canDisbandClan(final Player player, final Client client, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.LEADER))) {
                UtilMessage.message(player, "Clans", "You must be Leader to disband your Clan.");
                return false;
            }
            final Clan land = getManager().getClan(player.getLocation());
            if (land != null && clan.isEnemied(land)) {
                UtilMessage.message(player, "Clans", "You cannot disband your Clan while in enemy territory.");
                return false;
            }
            if (clan.canDomAvoid()) {
                UtilMessage.message(player, "Clans", "You cannot disband your Clan due to dom avoiding.");
                return false;
            }
            if (clan.isBeingPillaged()) {
                UtilMessage.message(player, "Clans", "You cannot disband your Clan while being conquered by another Clan.");
                return false;
            }
            if (!(this.getConfirmation().containsKey(clan.getName()))) {
                this.getConfirmation().put(clan.getName(), System.currentTimeMillis() + 15000L);
                UtilMessage.message(player, "Clans", "Re-type '" + ChatColor.AQUA + "/c disband" + ChatColor.GRAY + "' again to disband your Clan.");
                return false;
            }
            this.getConfirmation().remove(clan.getName());
        }
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanDisband(final ClanDisbandEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Clan clan = e.getClan();
        getManager().disbandClan(clan);
        if (e.getCause().equals(ClanDisbandEvent.DisbandCause.ENERGY)) {
            final String home = (clan.getHome() == null ? "" : " " + UtilLocation.locToString(clan.getHome()));
            UtilMessage.broadcast("Clans", ChatColor.YELLOW + clan.getName() + ChatColor.GRAY + " has disbanded due to running out of energy. " + home, null);
            return;
        }
        final Player player = e.getPlayer();
        UtilMessage.broadcast("Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has disbanded " + ChatColor.YELLOW + (clan instanceof AdminClan ? "Admin Clan " : "Clan ") + clan.getName() + ChatColor.GRAY + ".", null);
        Bukkit.getServer().getPluginManager().callEvent(new ScoreboardUpdateEvent(player));
    }

    @EventHandler
    public void onUpdate(final UpdateEvent e) {
        if (!(e.getType().equals(Updater.UpdateType.FAST))) {
            return;
        }
        if (this.getConfirmation().isEmpty()) {
            return;
        }
        for (final String clanName : this.getConfirmation().keySet()) {
            if (this.getConfirmation().get(clanName) > System.currentTimeMillis()) {
                continue;
            }
            this.getConfirmation().remove(clanName);
        }
    }
}