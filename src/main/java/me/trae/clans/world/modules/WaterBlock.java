package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.Core;
import me.trae.core.client.ClientManager;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.item.ItemBuilder;
import me.trae.core.item.events.ItemUpdateEvent;
import me.trae.core.utility.UtilBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

public class WaterBlock extends SpigotListener<Clans, WorldManager> {

    @ConfigInject(type = String.class, path = "Material", defaultValue = "LAPIS_BLOCK")
    private String material;

    public WaterBlock(final WorldManager manager) {
        super(manager);
    }

    private Material getMaterial() {
        return Material.valueOf(this.material);
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

        if (this.getInstanceByClass(Core.class).getManagerByClass(ClientManager.class).getClientByPlayer(event.getPlayer()).isAdministrating()) {
            return;
        }

        block.setType(Material.WATER);

        block.getState().update(true);

        UtilBlock.splash(block.getLocation());
    }

    @EventHandler
    public void onItemUpdate(final ItemUpdateEvent event) {
        final ItemBuilder builder = event.getBuilder();

        if (builder.getItemStack().getType() != this.getMaterial()) {
            return;
        }

        builder.setDisplayName(this.getName());

        builder.setLore(new String[]{
                "Transforms into Water upon placement."
        });
    }
}