package me.trae.clans.shop.shops.resources.items.currency;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.clans.weapon.weapons.items.currency.HundredThousandDisc;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.weapon.registry.WeaponRegistry;

public class HundredThousandDiscShopItem extends ShopItem<ResourcesShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "100_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "100_000")
    private int sellPrice;

    public HundredThousandDiscShopItem(final ResourcesShopKeeper module) {
        super(module, WeaponRegistry.getWeaponByClass(HundredThousandDisc.class).getFinalBuilder().toItemStack());
    }

    @Override
    public int getSlot() {
        return 34;
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