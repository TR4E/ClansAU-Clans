package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.ClanUnClaimAllEvent;
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

public class UnClaimAllCommand extends IClanCommand implements Listener {

    public UnClaimAllCommand(final ClanManager manager) {
        super(manager);
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
        if (!(this.canUnClaimAll(player, client, clan))) {
            return;
        }
        if (clan.getTerritory().size() <= 0) {
            UtilMessage.message(player, "Clans", "You do not own any Territory to un-claim.");
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new ClanUnClaimAllEvent(player, clan));
    }

    private boolean canUnClaimAll(final Player player, final Client client, final Clan clan) {
        if (!(client.isAdministrating())) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.LEADER))) {
                UtilMessage.message(player, "Clans", "You must be Leader to un-claim all land.");
                return false;
            }
        }
        return true;
    }

    private void unClaimAllLand(final Player player, final Clan clan) {
        if (clan.getHome() != null) {
            clan.setHome(null);
            getManager().getRepository().updateHome(clan);
        }
        clan.getTerritoryChunks().forEach(chunk -> getManager().unOutlineChunk(chunk));
        clan.getTerritory().clear();
        getManager().getRepository().updateTerritory(clan);
        UtilMessage.message(player, "Clans", "You unclaimed all your Territory.");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " unclaimed all Territory.", new UUID[]{player.getUniqueId()});
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanUnClaimAll(final ClanUnClaimAllEvent e) {
        if (e.isCancelled()) {
            return;
        }
        this.unClaimAllLand(e.getPlayer(), e.getClan());
    }
}