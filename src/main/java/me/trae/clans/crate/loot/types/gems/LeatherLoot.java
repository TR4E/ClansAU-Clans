package me.trae.clans.crate.loot.types.gems;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.ItemLoot;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LeatherLoot extends ItemLoot {

    public LeatherLoot(final Crate module, final double chance) {
        super(module, chance, new ItemStack(Material.LEATHER, 14));
    }
}