package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.ClanUnClaimEvent;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class UnClaimCommand extends IClanCommand implements Listener {

    public UnClaimCommand(final ClanManager manager) {
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
        if (land == null) {
            UtilMessage.message(player, "Clans", "This Territory is not owned by anyone.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Chunk chunk = player.getLocation().getChunk();
        if (!(this.canUnClaimLand(player, client, clan, land))) {
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new ClanUnClaimEvent(player, clan, land, chunk));
    }

    private boolean canUnClaimLand(final Player player, final Client client, final Clan clan, final Clan land) {
        if (!(client.isAdministrating())) {
            if (!(land.equals(clan))) {
                if (!((land.getTerritory().size() >= (land.getMembersMap().size() + 3)) || land.getTerritory().size() >= getManager().getClanModule().getPrimitiveCasted(Integer.class, "MaxClanClaims"))) {
                    UtilMessage.message(player, "Clans", "This Territory is not owned by you.");
                    return false;
                }
                if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                    UtilMessage.message(player, "Clans", "You must be an Admin or higher to un-claim land.");
                    return false;
                }
            }
        }
        return true;
    }

    private void unClaimLand(final Player player, final Clan clan, final Clan land, final Chunk chunk) {
        if (land.getHome() != null && land.getHome().getChunk().equals(chunk)) {
            land.setHome(null);
            getManager().getRepository().updateHome(land);
        }
        getManager().unOutlineChunk(chunk);
        land.removeTerritory(chunk);
        getManager().getRepository().updateTerritory(land);
        if (!(land instanceof AdminClan && land.equals(clan)) && ((land.getTerritory().size() >= (land.getMembersMap().size() + 3)) || land.getTerritory().size() >= getManager().getClanModule().getPrimitiveCasted(Integer.class, "MaxClanClaims"))) {
            UtilMessage.message(player, "Clans", "You unclaimed Territory " + UtilLocation.chunkToString(chunk) + ChatColor.GRAY + " from " + getManager().getClanRelation(clan, land).getSuffix() + getManager().getName(land, true) + ChatColor.GRAY + ".");
            land.messageClan("Clans", getManager().getClanRelation(land, clan).getSuffix() + getManager().getName(clan, true) + ChatColor.GRAY + " unclaimed your Territory " + UtilLocation.chunkToString(chunk) + ChatColor.GRAY + ".", null);
            return;
        }
        UtilMessage.message(player, "Clans", "You unclaimed Territory " + UtilLocation.chunkToString(chunk) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " unclaimed Territory " + UtilLocation.chunkToString(chunk) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanUnClaim(final ClanUnClaimEvent e) {
        if (e.isCancelled()) {
            return;
        }
        this.unClaimLand(e.getPlayer(), e.getClan(), e.getLand(), e.getChunk());
    }
}