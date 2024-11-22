package me.trae.clans.tnt.events;

import me.trae.clans.tnt.events.interfaces.ITNTExplodeEvent;
import me.trae.core.event.CustomCancellableEvent;
import me.trae.core.utility.UtilBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public class TNTExplodeEvent extends CustomCancellableEvent implements ITNTExplodeEvent {

    private final Location location;
    private final List<Block> blocks;

    public TNTExplodeEvent(final Location location, final double radius) {
        this.location = location;
        this.blocks = UtilBlock.createTNTExplosion(location, radius);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public List<Block> getBlocks() {
        return this.blocks;
    }

    @Override
    public void removeBlock(final Block block) {
        this.getBlocks().remove(block);
    }
}