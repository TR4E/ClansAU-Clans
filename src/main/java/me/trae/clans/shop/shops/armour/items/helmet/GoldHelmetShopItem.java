package me.trae.clans.shop.shops.armour.items.helmet;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.armour.ArmourShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoldHelmetShopItem extends ShopItem<ArmourShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "5_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "2_500")
    private int sellPrice;

    public GoldHelmetShopItem(final ArmourShopKeeper module) {
        super(module, new ItemStack(Material.GOLD_HELMET));
    }

    @Override
    public int getSlot() {
        return 15;
    }

    @Override
    public int getBuyPrice() {
        return 5000;
    }

    @Override
    public int getSellPrice() {
        return 2500;
    }
}