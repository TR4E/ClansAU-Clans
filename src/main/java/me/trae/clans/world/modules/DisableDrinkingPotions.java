package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.enums.ActionType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DisableDrinkingPotions extends SpigotListener<Clans, WorldManager> {

    public DisableDrinkingPotions(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!(ActionType.RIGHT_CLICK.isAction(event.getAction()))) {
            return;
        }

        final ItemStack itemStack = event.getItem();
        if (itemStack == null) {
            return;
        }

        if (itemStack.getType() != Material.POTION) {
            return;
        }

        event.setCancelled(true);
    }
}