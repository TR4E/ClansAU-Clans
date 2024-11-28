package me.trae.clans.crate.loot.models.interfaces;

import me.trae.core.weapon.Weapon;

public interface IWeaponLoot {

    Weapon<?, ?, ?> getWeapon();
}