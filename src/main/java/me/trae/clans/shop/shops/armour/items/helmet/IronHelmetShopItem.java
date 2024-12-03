package me.trae.clans.shop.shops.armour.items.helmet;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.armour.ArmourShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IronHelmetShopItem extends ShopItem<ArmourShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "5_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "2_500")
    private int sellPrice;

    public IronHelmetShopItem(final ArmourShopKeeper module) {
        super(module, new ItemStack(Material.IRON_HELMET));
    }

    @Override
    public int getSlot() {
        return 13;
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