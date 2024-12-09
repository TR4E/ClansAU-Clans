package me.trae.clans.perk.perks;

import me.trae.clans.perk.PerkManager;
import me.trae.clans.perk.perks.abstracts.SuperTool;
import me.trae.core.utility.UtilBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class SuperAxe extends SuperTool {

    public SuperAxe(final PerkManager manager) {
        super(manager);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Instantly mine any Wood Related Blocks in your own Clan Territory."
        };
    }

    @Override
    public Material getDefaultMaterial() {
        return Material.DIAMOND_AXE;
    }

    @Override
    public boolean isValidBlock(final Block block) {
        return UtilBlock.isWoodRelatedBlock(block);
    }
}