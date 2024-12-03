package me.trae.clans.fields.modules;

import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsManager;
import me.trae.clans.fields.data.FieldsBlock;
import me.trae.clans.fields.data.enums.FieldsBlockProperty;
import me.trae.clans.fields.events.FieldsBlockBreakEvent;
import me.trae.clans.fields.events.FieldsLootEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilItem;
import me.trae.core.utility.UtilServer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class HandleFieldsBlockBreak extends SpigotListener<Clans, FieldsManager> {

    public HandleFieldsBlockBreak(final FieldsManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();

        if (!(this.getManager().isInFields(block.getLocation()))) {
            return;
        }

        if (!(this.getManager().isFieldsBlock(block))) {
            return;
        }

        final FieldsBlock fieldsBlock = this.getManager().getBlockByLocation(block.getLocation());
        if (fieldsBlock == null) {
            return;
        }

        if (fieldsBlock.isBroken()) {
            return;
        }

        final Player player = event.getPlayer();

        if (this.getManager().isInAdminMode(player)) {
            return;
        }

        event.setCancelled(true);

        final FieldsBlockBreakEvent fieldsBlockBreakEvent = new FieldsBlockBreakEvent(fieldsBlock, block, player);
        UtilServer.callEvent(fieldsBlockBreakEvent);
        if (fieldsBlockBreakEvent.isCancelled()) {
            return;
        }

        fieldsBlock.setBroken(true);
        this.getManager().getRepository().updateData(fieldsBlock, FieldsBlockProperty.BROKEN);

        this.handleLoot(block);

        fieldsBlock.update(this.getManager());

        UtilItem.takeDurability(player, player.getEquipment().getItemInHand(), false, true);
    }

    private void handleLoot(final Block block) {
        final FieldsLootEvent event = new FieldsLootEvent(block.getType());
        UtilServer.callEvent(event);

        for (final ItemStack itemStack : event.getLoot()) {
            block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
        }
    }
}