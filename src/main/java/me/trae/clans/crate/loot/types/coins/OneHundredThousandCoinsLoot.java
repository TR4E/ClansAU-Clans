package me.trae.clans.crate.loot.types.coins;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.CoinsLoot;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class OneHundredThousandCoinsLoot extends CoinsLoot {

    public OneHundredThousandCoinsLoot(final Crate module, final double chance) {
        super(module, chance, new ItemStack(Material.GOLD_BLOCK), 100_000);
    }
}