package me.trae.clans.shop.shops.armour.items.boots;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.armour.ArmourShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoldBootsShopItem extends ShopItem<ArmourShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "4_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "2_000")
    private int sellPrice;

    public GoldBootsShopItem(final ArmourShopKeeper module) {
        super(module, new ItemStack(Material.GOLD_BOOTS));
    }

    @Override
    public int getSlot() {
        return 42;
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