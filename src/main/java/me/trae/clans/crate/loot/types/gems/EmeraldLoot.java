package me.trae.clans.crate.loot.types.gems;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.ItemLoot;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EmeraldLoot extends ItemLoot {

    public EmeraldLoot(final Crate module, final double chance) {
        super(module, chance, new ItemStack(Material.EMERALD, 14));
    }
}