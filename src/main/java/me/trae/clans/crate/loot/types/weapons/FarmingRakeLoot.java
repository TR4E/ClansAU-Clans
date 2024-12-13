package me.trae.clans.crate.loot.types.weapons;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.WeaponLoot;
import me.trae.clans.weapon.weapons.legendaries.FarmingRake;
import me.trae.core.weapon.registry.WeaponRegistry;

public class FarmingRakeLoot extends WeaponLoot {

    public FarmingRakeLoot(final Crate module, final double chance) {
        super(module, chance, WeaponRegistry.getWeaponByClass(FarmingRake.class));
    }
}