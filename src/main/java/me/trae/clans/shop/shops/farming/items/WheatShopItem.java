package me.trae.clans.shop.shops.farming.items;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.farming.FarmingShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WheatShopItem extends ShopItem<FarmingShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "54")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "24")
    private int sellPrice;

    public WheatShopItem(final FarmingShopKeeper module) {
        super(module, new ItemStack(Material.WHEAT));
    }

    @Override
    public int getSlot() {
        return 9;
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