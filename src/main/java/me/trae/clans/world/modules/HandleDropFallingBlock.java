package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilJava;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class HandleDropFallingBlock extends SpigotListener<Clans, WorldManager> {

    public HandleDropFallingBlock(final WorldManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof FallingBlock)) {
            return;
        }

        final FallingBlock fallingBlock = UtilJava.cast(FallingBlock.class, event.getEntity());

        final Material material = fallingBlock.getMaterial();

        if (!(Arrays.asList(Material.SAND, Material.GRAVEL).contains(material))) {
            return;
        }

        event.setCancelled(true);

        final Block block = event.getBlock();

        block.setType(Material.AIR);

        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(material));
    }
}