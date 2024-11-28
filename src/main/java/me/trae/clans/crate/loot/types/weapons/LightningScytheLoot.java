package me.trae.clans.crate.loot.types.weapons;

import me.trae.clans.crate.Crate;
import me.trae.clans.crate.loot.models.WeaponLoot;
import me.trae.clans.weapon.weapons.legendaries.LightningScythe;
import me.trae.core.weapon.registry.WeaponRegistry;

public class LightningScytheLoot extends WeaponLoot {

    public LightningScytheLoot(final Crate module, final double chance) {
        super(module, chance, WeaponRegistry.getWeaponByClass(LightningScythe.class));
    }
}