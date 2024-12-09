package me.trae.clans.shop.shops.weapons_and_tools.items.weapons;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.WeaponsAndToolsShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IronSwordShopItem extends ShopItem<WeaponsAndToolsShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "2_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "1_000")
    private int sellPrice;

    public IronSwordShopItem(final WeaponsAndToolsShopKeeper module) {
        super(module, new ItemStack(Material.IRON_SWORD));
    }

    @Override
    public int getSlot() {
        return 18;
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