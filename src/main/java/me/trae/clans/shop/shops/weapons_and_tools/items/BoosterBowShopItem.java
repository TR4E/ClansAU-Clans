package me.trae.clans.shop.shops.weapons_and_tools.items;

import me.trae.clans.shop.models.WeaponShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.WeaponsAndToolsShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.weapon.registry.WeaponRegistry;

public class BoosterBowShopItem extends WeaponShopItem<WeaponsAndToolsShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "300")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "150")
    private int sellPrice;

    public BoosterBowShopItem(final WeaponsAndToolsShopKeeper module) {
        super(module, WeaponRegistry.getWeaponByName("BoosterBow"));
    }

    @Override
    public int getSlot() {
        return 36;
    }

    @Override
    public int getBuyPrice() {
        return this.buyPrice;
    }

    @Override
    public int getSellPrice() {
        return this.sellPrice;
    }
}