package me.trae.clans.shop.shops.resources.items;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.item.constants.ItemConstants;

public class LapisLazuliShopItem extends ShopItem<ResourcesShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "500")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "100")
    private int sellPrice;

    public LapisLazuliShopItem(final ResourcesShopKeeper module) {
        super(module, ItemConstants.LAPIS_LAZULI);
    }

    @Override
    public int getSlot() {
        return 8;
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