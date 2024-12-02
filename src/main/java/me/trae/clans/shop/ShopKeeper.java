package me.trae.clans.shop;

import me.trae.clans.Clans;
import me.trae.clans.shop.interfaces.IShopKeeper;
import me.trae.core.framework.SpigotModule;

public abstract class ShopKeeper extends SpigotModule<Clans, ShopManager> implements IShopKeeper {

    public ShopKeeper(final ShopManager manager) {
        super(manager);
    }
}