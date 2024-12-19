package me.trae.clans.perk.perks.agilityhelmet;

import me.trae.clans.Clans;
import me.trae.clans.perk.perks.AgilityHelmet;
import me.trae.core.framework.types.frame.SpigotSubListener;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisableAgilityHelmetBlockPlace extends SpigotSubListener<Clans, AgilityHelmet> {

    public DisableAgilityHelmetBlockPlace(final AgilityHelmet module) {
        super(module);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();

        if (block.getType() != this.getModule().getMaterial()) {
            return;
        }

        event.setCancelled(true);
    }
}