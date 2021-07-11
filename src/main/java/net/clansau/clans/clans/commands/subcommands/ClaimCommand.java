package net.clansau.clans.clans.commands.subcommands;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.commands.framework.IClanCommand;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.clans.events.ClanClaimEvent;
import net.clansau.clans.config.OptionsManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.utility.UtilLocation;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClaimCommand extends IClanCommand implements Listener {

    public ClaimCommand(final ClanManager manager) {
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
        if (land != null && land.equals(clan)) {
            UtilMessage.message(player, "Clans", "This Territory is already owned by your Clan.");
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        final Chunk chunk = player.getLocation().getChunk();
        if (!(this.canClaimLand(player, client, clan, land, chunk))) {
            return;
        }
        Bukkit.getServer().getPluginManager().callEvent(new ClanClaimEvent(player, clan, land, chunk));
    }

    private boolean canClaimLand(final Player player, final Client client, final Clan clan, final Clan land, final Chunk chunk) {
        if (!(client.isAdministrating() || clan instanceof AdminClan)) {
            if (!(clan.hasClanRole(player.getUniqueId(), ClanRole.ADMIN))) {
                UtilMessage.message(player, "Clans", "You must be an Admin or higher to claim land.");
                return false;
            }
            if (!(player.getWorld().getName().equals("world"))) {
                UtilMessage.message(player, "Clans", "You cannot claim land in this World.");
                return false;
            }
            if (land != null) {
                UtilMessage.message(player, "Clans", "This Territory is owned by " + getManager().getClanRelation(clan, land).getSuffix() + getManager().getName(land, !(land instanceof AdminClan)) + ChatColor.GRAY + ".");
                return false;
            }
            for (final Entity entity : chunk.getEntities()) {
                if (!(entity instanceof Player)) {
                    continue;
                }
                final Player temp = (Player) entity;
                if (temp.equals(player) || !(getManager().canHurt(player, temp))) {
                    continue;
                }
                UtilMessage.message(player, "Clans", "You cannot claim land containing enemies.");
                return false;
            }
            if ((clan.getTerritory().size() >= (clan.getMembersMap().size() + 3)) || clan.getTerritory().size() >= getInstance().getManager(OptionsManager.class).getClansMaxClaims()) {
                UtilMessage.message(player, "Clans", "Your Clan cannot claim any more land.");
                return false;
            }
            final List<Clan> list = new ArrayList<>();
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    final Chunk c = player.getWorld().getChunkAt(chunk.getX() + x, chunk.getZ() + z);
                    final Clan cLand = getManager().getClan(c);
                    if (cLand == null) {
                        continue;
                    }
                    list.add(cLand);
                }
            }
            if (clan.getTerritory().size() > 0 && !(list.contains(clan))) {
                UtilMessage.message(player, "Clans", "You need to claim next to your own land.");
                return false;
            }
            for (final Clan found : list) {
                if (found != null && !(found.equals(clan))) {
                    UtilMessage.message(player, "Clans", "You cannot claim next to enemy land.");
                    return false;
                }
            }
        }
        return true;
    }

    private void claimLand(final Player player, final Clan clan, final Clan land, final Chunk chunk) {
        if (land != null) {
            if (land.getHome() != null && land.getHome().getChunk().equals(chunk)) {
                land.setHome(null);
                getManager().getRepository().updateHome(land);
            }
            getManager().unOutlineChunk(chunk);
            land.removeTerritory(chunk);
            getManager().getRepository().updateTerritory(land);
        }
        getManager().outlineChunk(clan, chunk, Material.GLOWSTONE, 120000L);
        clan.addTerritory(chunk);
        getManager().getRepository().updateTerritory(clan);
        UtilMessage.message(player, "Clans", "You claimed Territory " + UtilLocation.chunkToString(chunk) + ChatColor.GRAY + ".");
        clan.messageClan("Clans", ChatColor.AQUA + player.getName() + ChatColor.GRAY + " claimed Territory " + UtilLocation.chunkToString(chunk) + ChatColor.GRAY + ".", new UUID[]{player.getUniqueId()});
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanClaim(final ClanClaimEvent e) {
        if (e.isCancelled()) {
            return;
        }
        this.claimLand(e.getPlayer(), e.getClan(), e.getLand(), e.getChunk());
    }
}