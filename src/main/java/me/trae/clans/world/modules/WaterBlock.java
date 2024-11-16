package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.events.ItemUpdateEvent;
import me.trae.core.utility.UtilBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

public class WaterBlock extends SpigotListener<Clans, WorldManager> {

    public WaterBlock(final WorldManager manager) {
        super(manager);

        this.addPrimitive("Material", Material.LAPIS_BLOCK.name());
    }

    private Material getMaterial() {
        return Material.valueOf(this.getPrimitiveCasted(String.class, "Material"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Block block = event.getBlock();

        if (block.getType() != this.getMaterial()) {
            return;
        }

        block.setType(Material.WATER);

        block.getState().update(true);

        UtilBlock.splash(block.getLocation());
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        if (event.getBuilder().getItemStack().getType() != this.getMaterial()) {
            return;
        }

        event.getBuilder().setDisplayName(this.getName());
    }
}