package me.trae.clans.shop.models;

import me.trae.clans.shop.ShopKeeper;
import me.trae.core.weapon.Weapon;

public class CustomItemShopItem<M extends ShopKeeper> extends WeaponShopItem<M> {

    public CustomItemShopItem(final M module, final Weapon<?, ?, ?> weapon) {
        super(module, weapon);
    }

    @Override
    public boolean canStack() {
        return false;
    }
}