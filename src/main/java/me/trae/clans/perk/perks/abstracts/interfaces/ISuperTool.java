package me.trae.clans.perk.perks.abstracts.interfaces;

import org.bukkit.Material;
import org.bukkit.block.Block;

public interface ISuperTool {

    Material getDefaultMaterial();

    Material getMaterial();

    boolean isValidBlock(final Block block);
}