package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class DisableArmorStandInteract extends CoreListener<WorldManager> {

    public DisableArmorStandInteract(final WorldManager manager) {
        super(manager, "Disable Armor Stand Interact");
    }

    @EventHandler
    public void onArmorStandInteract(final PlayerArmorStandManipulateEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getRightClicked() == null) {
            return;
        }
        final Clan land = getManager().getClanManager().getClan(e.getRightClicked().getLocation());
        if (land == null) {
            return;
        }
        if (!(land instanceof AdminClan)) {
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