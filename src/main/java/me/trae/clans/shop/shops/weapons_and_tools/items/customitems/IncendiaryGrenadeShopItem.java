package me.trae.clans.shop.shops.weapons_and_tools.items.customitems;

import me.trae.clans.shop.models.CustomItemShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.WeaponsAndToolsShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.weapon.registry.WeaponRegistry;

public class IncendiaryGrenadeShopItem extends CustomItemShopItem<WeaponsAndToolsShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "1_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "500")
    private int sellPrice;

    public IncendiaryGrenadeShopItem(final WeaponsAndToolsShopKeeper module) {
        super(module, WeaponRegistry.getWeaponByName("IncendiaryGrenade"));
    }

    @Override
    public int getSlot() {
        return 41;
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