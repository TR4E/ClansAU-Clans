package net.clansau.clans.world.listeners;

import net.clansau.clans.clans.AdminClan;
import net.clansau.clans.clans.Clan;
import net.clansau.clans.world.WorldManager;
import net.clansau.core.client.Client;
import net.clansau.core.client.ClientManager;
import net.clansau.core.framework.modules.CoreListener;
import net.clansau.core.utility.UtilMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

public class DisableShootingArrowsInSafeZones extends CoreListener<WorldManager> {

    public DisableShootingArrowsInSafeZones(final WorldManager manager) {
        super(manager, "Disable Shoot Bows In Safe Zones");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBowShoot(final EntityShootBowEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player) e.getEntity();
        final Clan land = getManager().getClanManager().getClan(player.getLocation());
        if (land == null) {
            return;
        }
        if (!(land instanceof AdminClan && ((AdminClan) land).isSafe())) {
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
        UtilMessage.message(player, "Restrictions", "You are not allowed to shoot bows in safe zones!");
    }
}