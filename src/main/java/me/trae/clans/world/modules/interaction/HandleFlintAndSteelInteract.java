package me.trae.clans.world.modules.interaction;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class HandleFlintAndSteelInteract extends SpigotListener<Clans, WorldManager> {

    public HandleFlintAndSteelInteract(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        if (Arrays.asList(Material.NETHERRACK, Material.TNT).contains(block.getType())) {
            return;
        }

        final ItemStack itemStack = event.getItem();
        if (itemStack == null) {
            return;
        }

        if (itemStack.getType() != Material.FLINT_AND_STEEL) {
            return;
        }

        final Player player = event.getPlayer();

        if (this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
            return;
        }

        event.setCancelled(true);

        UtilMessage.simpleMessage(player, "Game", "You cannot use <yellow><var></yellow> on <green><var></green>.", Arrays.asList(UtilString.clean(itemStack.getType().name()), UtilString.clean(block.getType().name())));
    }
}