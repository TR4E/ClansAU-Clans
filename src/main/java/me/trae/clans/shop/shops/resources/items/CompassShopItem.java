package me.trae.clans.shop.shops.resources.items;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CompassShopItem extends ShopItem<ResourcesShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "2_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "1_000")
    private int sellPrice;

    public CompassShopItem(final ResourcesShopKeeper module) {
        super(module, new ItemStack(Material.COMPASS));
    }

    @Override
    public int getSlot() {
        return 31;
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