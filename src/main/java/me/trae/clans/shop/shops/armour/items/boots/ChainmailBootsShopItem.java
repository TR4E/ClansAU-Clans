package me.trae.clans.shop.shops.armour.items.boots;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.armour.ArmourShopKeeper;
import me.trae.core.config.annotations.ConfigInject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChainmailBootsShopItem extends ShopItem<ArmourShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "4_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "2_500")
    private int sellPrice;

    public ChainmailBootsShopItem(final ArmourShopKeeper module) {
        super(module, new ItemStack(Material.CHAINMAIL_BOOTS));
    }

    @Override
    public int getSlot() {
        return 44;
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