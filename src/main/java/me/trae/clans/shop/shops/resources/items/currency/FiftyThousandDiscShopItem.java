package me.trae.clans.shop.shops.resources.items.currency;

import me.trae.clans.shop.ShopItem;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.clans.weapon.weapons.items.currency.FiftyThousandDisc;
import me.trae.core.config.annotations.ConfigInject;
import me.trae.core.weapon.registry.WeaponRegistry;

public class FiftyThousandDiscShopItem extends ShopItem<ResourcesShopKeeper> {

    @ConfigInject(type = Integer.class, path = "Buy-Price", defaultValue = "50_000")
    private int buyPrice;

    @ConfigInject(type = Integer.class, path = "Sell-Price", defaultValue = "50_000")
    private int sellPrice;

    public FiftyThousandDiscShopItem(final ResourcesShopKeeper module) {
        super(module, WeaponRegistry.getWeaponByClass(FiftyThousandDisc.class).getBuilder().toItemStack());
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