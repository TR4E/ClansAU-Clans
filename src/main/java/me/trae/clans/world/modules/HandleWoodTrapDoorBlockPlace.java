package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class HandleWoodTrapDoorBlockPlace extends SpigotListener<Clans, WorldManager> {

    public HandleWoodTrapDoorBlockPlace(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();

        if (block.getType() != Material.TRAP_DOOR) {
            return;
        }

        final Player player = event.getPlayer();

        if (this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
            return;
        }

        block.setType(Material.AIR);

        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_TRAPDOOR));

        UtilMessage.simpleMessage(player, "Game", "Please use <yellow>Iron Trap Doors</yellow>.");
    }
}