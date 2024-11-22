package me.trae.clans.tnt.events.interfaces;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public interface ITNTExplodeEvent {

    Location getLocation();

    List<Block> getBlocks();

    void removeBlock(final Block block);
}