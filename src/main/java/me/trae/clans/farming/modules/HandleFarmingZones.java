package me.trae.clans.farming.modules;

import me.trae.clans.Clans;
import me.trae.clans.farming.FarmingManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class HandleFarmingZones extends SpigotListener<Clans, FarmingManager> {

    @ConfigInject(type = Integer.class, path = "Max-Y", defaultValue = "60")
    private int maxY;

    @ConfigInject(type = Integer.class, path = "Min-Y", defaultValue = "44")
    private int minY;

    public HandleFarmingZones(final FarmingManager manager) {
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

        final Player player = event.getPlayer();

        final ItemStack itemStack = event.getItem();
        if (itemStack == null) {
            return;
        }

        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        if (block.getLocation().getBlockY() <= this.maxY && block.getLocation().getBlockY() >= this.minY) {
            return;
        }

        if (this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
            return;
        }

        switch (block.getType()) {
            case GRASS:
            case DIRT: {
                if (UtilItem.isHoe(itemStack.getType())) {
                    event.setCancelled(true);
                    this.inform(player);
                }
                break;
            }

            case SOIL: {
                if (Arrays.asList(Material.SEEDS, Material.MELON_SEEDS, Material.PUMPKIN_SEEDS, Material.CARROT_ITEM, Material.POTATO_ITEM).contains(itemStack.getType())) {
                    event.setCancelled(true);
                    this.inform(player);
                }
                break;
            }

            case SOUL_SAND: {
                if (itemStack.getType() == Material.NETHER_WARTS) {
                    event.setCancelled(true);
                    this.inform(player);
                }
                break;
            }

            case SAND: {
                if (Arrays.asList(Material.CACTUS, Material.SUGAR_CANE).contains(itemStack.getType())) {
                    event.setCancelled(true);
                    this.inform(player);
                }
                break;
            }
        }
    }

    private void inform(final Player player) {
        player.updateInventory();

        UtilMessage.simpleMessage(player, "Agriculture", "You can only cultivate between <yellow><var></yellow> and <yellow><var></yellow> Y levels!", Arrays.asList(String.valueOf(this.minY), String.valueOf(this.maxY)));
    }
}