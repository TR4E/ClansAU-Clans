package me.trae.clans.crate.loot.types;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.ItemLoot;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TntLoot extends ItemLoot {

    public TntLoot(final Crate module, final double chance) {
        super(module, chance, new ItemStack(Material.TNT, 1));
    }
}