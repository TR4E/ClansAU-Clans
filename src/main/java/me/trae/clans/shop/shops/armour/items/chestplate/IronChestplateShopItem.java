package me.trae.clans.shop.shops.armour.items.chestplate;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.armour.ArmourShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IronChestplateShopItem extends ShopItem<ArmourShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "8_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "4000")
    private int sellPrice;

    public IronChestplateShopItem(final ArmourShopKeeper module) {
        super(module, new ItemStack(Material.IRON_CHESTPLATE));
    }

    @Override
    public int getSlot() {
        return 22;
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