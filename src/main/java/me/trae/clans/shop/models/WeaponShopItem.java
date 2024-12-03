package me.trae.clans.shop.models;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.models.interfaces.IWeaponShopItem;
import me.trae.core.item.ItemBuilder;
import me.trae.core.weapon.Weapon;

public class WeaponShopItem<M extends ShopKeeper> extends ShopItem<M> implements IWeaponShopItem {

    private final Weapon<?, ?, ?> weapon;

    public WeaponShopItem(final M module, final Weapon<?, ?, ?> weapon) {
        super(module, weapon.getBuilder().toItemStack());

        this.weapon = weapon;
    }

    @Override
    public Weapon<?, ?, ?> getWeapon() {
        return this.weapon;
    }

    @Override
    public ItemBuilder toItemBuilder() {
        return this.getWeapon().getFinalBuilder();
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public int getBuyPrice() {
        return 0;
    }

    @Override
    public int getSellPrice() {
        return 0;
    }
}