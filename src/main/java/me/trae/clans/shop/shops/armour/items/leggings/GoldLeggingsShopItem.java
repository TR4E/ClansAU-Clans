package me.trae.clans.shop.shops.armour.items.leggings;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.armour.ArmourShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GoldLeggingsShopItem extends ShopItem<ArmourShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "7_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "3_500")
    private int sellPrice;

    public GoldLeggingsShopItem(final ArmourShopKeeper module) {
        super(module, new ItemStack(Material.GOLD_LEGGINGS));
    }

    @Override
    public int getSlot() {
        return 33;
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