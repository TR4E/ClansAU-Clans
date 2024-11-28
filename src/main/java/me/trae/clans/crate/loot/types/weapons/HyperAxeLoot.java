package me.trae.clans.crate.loot.types.weapons;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.WeaponLoot;
import me.trae.clans.weapon.weapons.legendaries.HyperAxe;
import me.trae.core.weapon.registry.WeaponRegistry;

public class HyperAxeLoot extends WeaponLoot {

    public HyperAxeLoot(final Crate module, final double chance) {
        super(module, chance, WeaponRegistry.getWeaponByClass(HyperAxe.class));
    }
}