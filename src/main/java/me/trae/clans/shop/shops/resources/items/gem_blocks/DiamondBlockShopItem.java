package me.trae.clans.shop.shops.resources.items.gem_blocks;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondBlockShopItem extends ShopItem<ResourcesShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "9_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "2_250")
    private int sellPrice;

    public DiamondBlockShopItem(final ResourcesShopKeeper module) {
        super(module, new ItemStack(Material.DIAMOND_BLOCK));
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