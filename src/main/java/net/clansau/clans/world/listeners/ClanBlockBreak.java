package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilFormat;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class ClanBlockBreak extends CoreListener<WorldManager> {

    public ClanBlockBreak(final WorldManager manager) {
        super(manager, "Clan Block Break");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(final BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Block block = e.getBlock();
        if (block == null) {
            return;
        }
        final Player player = e.getPlayer();
        final ClanManager clanManager = getManager().getClanManager();
        final Clan land = clanManager.getClan(block.getLocation());
        final Clan clan = clanManager.getClan(player.getUniqueId());
        if (this.canBreakBlock(player, clan, land)) {
            return;
        }
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isAdministrating()) {
            return;
        }
        e.setCancelled(true);
        UtilMessage.message(player, "Clans", "You cannot break " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().name()) + ChatColor.GRAY + " in " + clanManager.getClanRelation(clan, land).getSuffix() + clanManager.getName(land, !(land instanceof AdminClan)) + ChatColor.GRAY + ".");
    }

    private boolean canBreakBlock(final Player player, final Clan clan, final Clan land) {
        if (land == null) {
            return true;
        }
        if (clan != null) {
            if (clan.equals(land)) {
                return !(clan.getClanRole(player.getUniqueId()).equals(ClanRole.RECRUIT));
            }
            return clan.isPillaging(land);
        }
        return false;
    }
}