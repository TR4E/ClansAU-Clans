package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

public class DisableBucketInteraction extends SpigotListener<Clans, WorldManager> {

    public DisableBucketInteraction(final WorldManager manager) {
        super(manager);
    }

    private void cancel(final PlayerBucketEvent event) {
        final Player player = event.getPlayer();

        if (this.getInstance(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(player).isAdministrating()) {
            return;
        }

        event.setCancelled(true);

        event.getBlockClicked().getState().update(true);

        final ItemStack itemStack = player.getEquipment().getItemInHand().clone();

        player.getEquipment().setItemInHand(null);

        UtilItem.insert(player, new ItemStack(Material.IRON_INGOT, itemStack.getAmount() * 3));

        UtilMessage.simpleMessage(player, "Game", "Your <yellow>Bucket</yellow> broke!");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBucketFill(final PlayerBucketFillEvent event) {
        if (event.isCancelled()) {
            return;
        }

        this.cancel(event);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        if (event.isCancelled()) {
            return;
        }

        this.cancel(event);
    }
}