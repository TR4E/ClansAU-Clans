package me.trae.clans.shop.shops.farming.items.blocks;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.farming.FarmingShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MelonBlockShopItem extends ShopItem<FarmingShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "75")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "30")
    private int sellPrice;

    public MelonBlockShopItem(final FarmingShopKeeper module) {
        super(module, new ItemStack(Material.MELON_BLOCK));
    }

    @Override
    public int getSlot() {
        return 36;
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