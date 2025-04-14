package me.trae.clans.gamer.modules.blocks;

import me.trae.clans.Clans;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilJava;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class HandleGamerBlockBreak extends SpigotListener<Clans, GamerManager> {

    public HandleGamerBlockBreak(final GamerManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        UtilJava.call(this.getManager().getGamerByPlayer(event.getPlayer()), gamer -> {
            gamer.setBlocksBroken(gamer.getBlocksBroken() + 1);

            this.getManager().addDelayedSave(gamer, GamerProperty.BLOCKS_BROKEN);
        });
    }
}