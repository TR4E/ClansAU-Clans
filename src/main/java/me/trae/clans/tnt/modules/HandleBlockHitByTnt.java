package me.trae.clans.tnt.modules;

import me.trae.clans.Clans;
import me.trae.clans.tnt.TntManager;
import me.trae.clans.tnt.data.ReplacementBlock;
import me.trae.clans.tnt.events.TNTExplodeEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.utility.UtilJava;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.HashMap;
import java.util.Map;

public class HandleBlockHitByTnt extends SpigotListener<Clans, TntManager> {

    private final Map<ReplacementBlock, ReplacementBlock> MAP = UtilJava.createMap(new HashMap<>(), map -> {
        // Stone Brick
        map.put(new ReplacementBlock(Material.SMOOTH_BRICK, 0), new ReplacementBlock(Material.SMOOTH_BRICK, 2));
        map.put(new ReplacementBlock(Material.SMOOTH_BRICK, 2), new ReplacementBlock(Material.AIR));

        // Nether Brick
        map.put(new ReplacementBlock(Material.NETHER_BRICK), new ReplacementBlock(Material.NETHERRACK));
        map.put(new ReplacementBlock(Material.NETHERRACK), new ReplacementBlock(Material.AIR));

        // Sandstone
        map.put(new ReplacementBlock(Material.SANDSTONE, 2), new ReplacementBlock(Material.SANDSTONE, 0));
        map.put(new ReplacementBlock(Material.SANDSTONE, 0), new ReplacementBlock(Material.AIR));

        // Red Sandstone
        map.put(new ReplacementBlock(Material.RED_SANDSTONE, 2), new ReplacementBlock(Material.RED_SANDSTONE, 0));
        map.put(new ReplacementBlock(Material.RED_SANDSTONE, 0), new ReplacementBlock(Material.AIR));

        // Quartz
        map.put(new ReplacementBlock(Material.QUARTZ_BLOCK, 0), new ReplacementBlock(Material.QUARTZ_BLOCK, 1));
        map.put(new ReplacementBlock(Material.QUARTZ_BLOCK, 1), new ReplacementBlock(Material.AIR));

        // Prismarine
        map.put(new ReplacementBlock(Material.PRISMARINE, 2), new ReplacementBlock(Material.PRISMARINE, 1));
        map.put(new ReplacementBlock(Material.PRISMARINE, 1), new ReplacementBlock(Material.PRISMARINE, 0));
        map.put(new ReplacementBlock(Material.PRISMARINE, 0), new ReplacementBlock(Material.AIR));
    });

    public HandleBlockHitByTnt(final TntManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTNTExplode(final TNTExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        event.getBlocks().removeIf(block -> {
            if (block.isLiquid()) {
                block.setType(Material.AIR);
                return true;
            }

            for (final Map.Entry<ReplacementBlock, ReplacementBlock> entry : this.MAP.entrySet()) {
                final ReplacementBlock key = entry.getKey();

                if (block.getType() == key.getMaterial() && block.getData() == key.getData()) {
                    final ReplacementBlock value = entry.getValue();

                    if (value.getMaterial() == Material.AIR) {
                        return false;
                    }

                    block.setTypeIdAndData(value.getMaterial().getId(), value.getData(), true);
                    return true;
                }
            }

            return false;
        });
    }
}