package net.clansau.clans.clans.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.clans.ClanManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class PillageDisableBlockBreak extends CoreListener<ClanManager> {

    public PillageDisableBlockBreak(final ClanManager manager) {
        super(manager, "Pillage Disable Block Break");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Block block = e.getBlock();
        if (block == null) {
            return;
        }
        final Clan land = getManager().getClan(block.getLocation());
        if (land == null) {
            return;
        }
        if (!(land.isBeingPillaged())) {
            return;
        }
        final Player player = e.getPlayer();
        if (!(land.isMember(player.getUniqueId()))) {
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
        UtilMessage.message(player, "Clans", "You cannot break blocks while being conquered by another Clan.");
    }
}