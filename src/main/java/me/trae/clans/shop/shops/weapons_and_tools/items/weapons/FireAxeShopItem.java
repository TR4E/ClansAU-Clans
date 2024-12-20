package me.trae.clans.shop.shops.weapons_and_tools.items.weapons;

import me.trae.clans.shop.models.WeaponShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.WeaponsAndToolsShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.weapon.registry.WeaponRegistry;

public class FireAxeShopItem extends WeaponShopItem<WeaponsAndToolsShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "10_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "0")
    private int sellPrice;

    public FireAxeShopItem(final WeaponsAndToolsShopKeeper module) {
        super(module, WeaponRegistry.getWeaponByName("FireAxe"));
    }

    @Override
    public int getSlot() {
        return 12;
    }

    @Override
    public int getBuyPrice() {
        return this.buyPrice;
    }

    @Override
    public int getSellPrice() {
        return this.sellPrice;
    }

    @Override
    public boolean canStack() {
        return false;
    }
}