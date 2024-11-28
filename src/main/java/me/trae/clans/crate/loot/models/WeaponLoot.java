package me.trae.clans.crate.loot.models;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.interfaces.IWeaponLoot;
import me.trae.core.weapon.Weapon;
import org.bukkit.inventory.ItemStack;

public class WeaponLoot extends ItemLoot implements IWeaponLoot {

    private final Weapon<?, ?, ?> weapon;

    public WeaponLoot(final Crate module, final double chance, final Weapon<?, ?, ?> weapon) {
        super(module, chance, weapon.getBuilder().toItemStack());

        this.weapon = weapon;
    }

    @Override
    public boolean isBroadcast() {
        return true;
    }

    @Override
    public Weapon<?, ?, ?> getWeapon() {
        return this.weapon;
    }

    @Override
    public ItemStack toItemStack() {
        return this.getWeapon().getFinalBuilder().toItemStack();
    }
}