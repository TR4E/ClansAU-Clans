package me.trae.clans.shop.shops.armour.items.helmet;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.armour.ArmourShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LeatherHelmetShopItem extends ShopItem<ArmourShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "5_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "2_500")
    private int sellPrice;

    public LeatherHelmetShopItem(final ArmourShopKeeper module) {
        super(module, new ItemStack(Material.LEATHER_HELMET));
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