package me.trae.clans.shop.shops.weapons_and_tools.items.customitems;

import me.trae.clans.shop.models.CustomItemShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.WeaponsAndToolsShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.weapon.registry.WeaponRegistry;

public class EtherealPearlShopItem extends CustomItemShopItem<WeaponsAndToolsShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "2_500")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "1_000")
    private int sellPrice;

    public EtherealPearlShopItem(final WeaponsAndToolsShopKeeper module) {
        super(module, WeaponRegistry.getWeaponByName("EtherealPearl"));
    }

    @Override
    public int getSlot() {
        return 38;
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