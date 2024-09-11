package me.trae.clans.weapon.weapons;

import me.trae.clans.Clans;
import me.trae.clans.weapon.WeaponManager;
import me.trae.core.weapon.data.WeaponData;
import me.trae.core.weapon.types.Legendary;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WindBlade extends Legendary<Clans, WeaponManager, WeaponData> {

    public WindBlade(final WeaponManager manager) {
        super(manager, new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public Class<WeaponData> getClassOfData() {
        return WeaponData.class;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }
}