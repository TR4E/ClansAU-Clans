package me.trae.clans.shop.shops.resources.items.gems;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EmeraldShopItem extends ShopItem<ResourcesShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "1_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "250")
    private int sellPrice;

    public EmeraldShopItem(final ResourcesShopKeeper module) {
        super(module, new ItemStack(Material.EMERALD));
    }

    @Override
    public int getSlot() {
        return 3;
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