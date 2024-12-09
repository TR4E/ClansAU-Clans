package me.trae.clans.shop.shops.weapons_and_tools.items.tools;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.WeaponsAndToolsShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondHoeShopItem extends ShopItem<WeaponsAndToolsShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "2_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "1_000")
    private int sellPrice;

    public DiamondHoeShopItem(final WeaponsAndToolsShopKeeper module) {
        super(module, new ItemStack(Material.DIAMOND_HOE));
    }

    @Override
    public int getSlot() {
        return 8;
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