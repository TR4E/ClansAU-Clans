package me.trae.clans.shop.shops.building.items.stone;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.building.BuildingShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CobblestoneShopItem extends ShopItem<BuildingShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "20")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "5")
    private int sellPrice;

    public CobblestoneShopItem(final BuildingShopKeeper module) {
        super(module, new ItemStack(Material.COBBLESTONE));
    }

    @Override
    public int getSlot() {
        return 1;
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