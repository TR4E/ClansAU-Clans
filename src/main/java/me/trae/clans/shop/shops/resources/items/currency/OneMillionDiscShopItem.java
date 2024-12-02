package me.trae.clans.shop.shops.resources.items.currency;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.clans.weapon.weapons.items.currency.OneMillionDisc;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.weapon.registry.WeaponRegistry;

public class OneMillionDiscShopItem extends ShopItem<ResourcesShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "1_000_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "1_000_000")
    private int sellPrice;

    public OneMillionDiscShopItem(final ResourcesShopKeeper module) {
        super(module, WeaponRegistry.getWeaponByClass(OneMillionDisc.class).getFinalBuilder().toItemStack());
    }

    @Override
    public int getSlot() {
        return 35;
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