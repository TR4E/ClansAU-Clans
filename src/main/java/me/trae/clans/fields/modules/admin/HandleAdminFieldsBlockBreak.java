package me.trae.clans.fields.modules.admin;

import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsBlock;
import me.trae.clans.fields.FieldsManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;

public class HandleAdminFieldsBlockBreak extends SpigotListener<Clans, FieldsManager> {

    public HandleAdminFieldsBlockBreak(final FieldsManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
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

        final Player player = event.getPlayer();

        if (!(this.getManager().isInAdminMode(player))) {
            return;
        }

        event.setCancelled(true);

        this.getManager().removeBlock(fieldsBlock);
        this.getManager().getRepository().deleteData(fieldsBlock);

        UtilMessage.simpleMessage(player, "Fields", UtilString.pair("Deleted Block", "<green><var> <var>"), Arrays.asList(UtilBlock.getDisplayName(block), UtilLocation.locationToString(block.getLocation())));

        block.setType(Material.AIR);
    }
}