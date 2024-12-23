package me.trae.clans.shop.shops.farming.items.seeds;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.farming.FarmingShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PumpkinSeedsShopItem extends ShopItem<FarmingShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "5")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "1")
    private int sellPrice;

    public PumpkinSeedsShopItem(final FarmingShopKeeper module) {
        super(module, new ItemStack(Material.PUMPKIN_SEEDS));
    }

    @Override
    public int getSlot() {
        return 2;
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