package me.trae.clans.shop.shops.weapons_and_tools.items.weapons;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.WeaponsAndToolsShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoldSwordShopItem extends ShopItem<WeaponsAndToolsShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "5_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "2_500")
    private int sellPrice;

    public GoldSwordShopItem(final WeaponsAndToolsShopKeeper module) {
        super(module, new ItemStack(Material.GOLD_SWORD));
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