package me.trae.clans.fields.modules.admin;

import me.trae.clans.Clans;
import me.trae.clans.fields.data.FieldsBlock;
import me.trae.clans.fields.FieldsManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilBlock;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilString;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;

public class HandleAdminFieldsBlockPlace extends SpigotListener<Clans, FieldsManager> {

    public HandleAdminFieldsBlockPlace(final FieldsManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(final BlockPlaceEvent event) {
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

        final Player player = event.getPlayer();

        if (!(this.getManager().isInAdminMode(player))) {
            return;
        }

        final FieldsBlock fieldsBlock = new FieldsBlock(block);

        this.getManager().addBlock(fieldsBlock);
        this.getManager().getRepository().saveData(fieldsBlock);

        UtilMessage.simpleMessage(player, "Fields", UtilString.pair("Added Block", "<green><var> <var>"), Arrays.asList(UtilBlock.getDisplayName(block), UtilLocation.locationToString(block.getLocation())));
    }
}