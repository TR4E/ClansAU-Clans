package me.trae.clans.tnt.modules;

import me.trae.clans.Clans;
import me.trae.clans.tnt.TntManager;
import me.trae.clans.tnt.events.TNTExplodeEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Arrays;
import java.util.List;

public class HandleBlacklistedBlocksOnTntExplode extends SpigotListener<Clans, TntManager> {

    private final List<Material> MATERIALS = Arrays.asList(Material.BARRIER, Material.BEDROCK, Material.OBSIDIAN);

    public HandleBlacklistedBlocksOnTntExplode(final TntManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTNTExplode(final TNTExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        event.getBlocks().removeIf(block -> this.MATERIALS.contains(block.getType()));
    }
}