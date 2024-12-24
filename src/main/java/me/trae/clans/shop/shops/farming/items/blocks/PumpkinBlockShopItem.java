package me.trae.clans.shop.shops.farming.items.blocks;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.farming.FarmingShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PumpkinBlockShopItem extends ShopItem<FarmingShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "70")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "20")
    private int sellPrice;

    public PumpkinBlockShopItem(final FarmingShopKeeper module) {
        super(module, new ItemStack(Material.PUMPKIN));
    }

    @Override
    public int getSlot() {
        return 37;
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