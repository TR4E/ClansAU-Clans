package me.trae.clans.shop.shops.building.items.dirt;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.building.BuildingShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GravelShopItem extends ShopItem<BuildingShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "100")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "20")
    private int sellPrice;

    public GravelShopItem(final BuildingShopKeeper module) {
        super(module, new ItemStack(Material.GRAVEL));
    }

    @Override
    public int getSlot() {
        return 45;
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