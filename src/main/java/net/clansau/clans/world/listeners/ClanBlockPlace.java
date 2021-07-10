package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.clans.clans.enums.ClanRole;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilBlock;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

public class ClanBlockPlace extends CoreListener<WorldManager> {

    public ClanBlockPlace(final WorldManager manager) {
        super(manager, "Clan Block Place");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent e) {
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
        if (this.canPlaceBlock(player, clan, land)) {
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
        UtilMessage.message(player, "Clans", "You cannot place " + ChatColor.GREEN + UtilBlock.getCleanName(block) + ChatColor.GRAY + " in " + clanManager.getClanRelation(clan, land).getSuffix() + clanManager.getName(land, !(land instanceof AdminClan)) + ChatColor.GRAY + ".");
    }

    private boolean canPlaceBlock(final Player player, final Clan clan, final Clan land) {
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