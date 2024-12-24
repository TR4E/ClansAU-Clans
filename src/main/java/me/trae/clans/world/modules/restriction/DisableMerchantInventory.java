package me.trae.clans.world.modules.restriction;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class DisableMerchantInventory extends SpigotListener<Clans, WorldManager> {

    public DisableMerchantInventory(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryOpen(final InventoryOpenEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getInventory().getType() != InventoryType.MERCHANT) {
            return;
        }

        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        if (this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(event.getActor()).isAdministrating()) {
            return;
        }

        event.setCancelled(true);
    }
}