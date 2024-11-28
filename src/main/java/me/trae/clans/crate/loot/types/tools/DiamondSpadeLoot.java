package me.trae.clans.crate.loot.types.tools;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.ItemLoot;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondSpadeLoot extends ItemLoot {

    public DiamondSpadeLoot(final Crate module, final double chance) {
        super(module, chance, new ItemStack(Material.DIAMOND_SPADE, 1));
    }
}