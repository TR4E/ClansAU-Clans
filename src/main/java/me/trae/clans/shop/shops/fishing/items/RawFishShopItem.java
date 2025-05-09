package me.trae.clans.shop.shops.fishing.items;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.fishing.FishingShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RawFishShopItem extends ShopItem<FishingShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "24")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "12")
    private int sellPrice;

    public RawFishShopItem(final FishingShopKeeper module) {
        super(module, new ItemStack(Material.RAW_FISH));
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

    @Override
    public boolean isSimilarWithItemMeta() {
        return false;
    }
}