package me.trae.clans.shop.shops.weapons_and_tools.items.tools;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.WeaponsAndToolsShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DiamondSpadeShopItem extends ShopItem<WeaponsAndToolsShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "1_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "500")
    private int sellPrice;

    public DiamondSpadeShopItem(final WeaponsAndToolsShopKeeper module) {
        super(module, new ItemStack(Material.DIAMOND_SPADE));
    }

    @Override
    public int getSlot() {
        return 6;
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