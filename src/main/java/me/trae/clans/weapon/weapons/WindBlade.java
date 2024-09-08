package me.trae.clans.weapon.weapons;

import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WindBlade extends Legendary<Clans, WeaponManager> {

    public WindBlade(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_SWORD));
    }
}