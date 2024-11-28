package me.trae.clans.crate.loot.types.weapons;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.WeaponLoot;
import me.trae.clans.weapon.weapons.legendaries.GiantsBroadsword;
import me.trae.core.weapon.registry.WeaponRegistry;

public class GiantsBroadswordLoot extends WeaponLoot {

    public GiantsBroadswordLoot(final Crate module, final double chance) {
        super(module, chance, WeaponRegistry.getWeaponByClass(GiantsBroadsword.class));
    }
}