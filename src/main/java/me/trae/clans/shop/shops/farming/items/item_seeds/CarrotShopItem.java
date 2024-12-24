package me.trae.clans.shop.shops.farming.items.item_seeds;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.farming.FarmingShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CarrotShopItem extends ShopItem<FarmingShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "45")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "25")
    private int sellPrice;

    public CarrotShopItem(final FarmingShopKeeper module) {
        super(module, new ItemStack(Material.CARROT_ITEM));
    }

    @Override
    public int getSlot() {
        return 19;
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