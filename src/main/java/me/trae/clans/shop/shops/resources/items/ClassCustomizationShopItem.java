package me.trae.clans.shop.shops.resources.items;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ClassCustomizationShopItem extends ShopItem<ResourcesShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "100_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "50_000")
    private int sellPrice;

    public ClassCustomizationShopItem(final ResourcesShopKeeper module) {
        super(module, new ItemStack(Material.ENCHANTMENT_TABLE));
    }

    @Override
    public int getSlot() {
        return 49;
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