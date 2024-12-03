package me.trae.clans.shop.models.interfaces;

import me.trae.core.weapon.Weapon;

public interface IWeaponShopItem {

    Weapon<?, ?, ?> getWeapon();
}