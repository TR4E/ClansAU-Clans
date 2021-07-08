package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.Clan;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DisableSoilChange extends CoreListener<WorldManager> {

    public DisableSoilChange(final WorldManager manager) {
        super(manager, "Disable Soil Change");
    }

    @EventHandler
    public void onSoilChange(final PlayerInteractEvent e) {
        if (!(e.getAction().equals(Action.PHYSICAL))) {
            return;
        }
        final Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        if (!(block.getType().equals(Material.SOIL))) {
            return;
        }
        final Clan land = getManager().getClanManager().getClan(block.getLocation());
        if (land == null) {
            return;
        }
        final Player player = e.getPlayer();
        final Client client = getInstance().getManager(ClientManager.class).getOnlineClient(player.getUniqueId());
        if (client == null) {
            return;
        }
        if (client.isAdministrating()) {
            return;
        }
        e.setCancelled(true);
    }
}