package me.trae.clans.shop;

import me.trae.clans.Clans;
import me.trae.clans.shop.commands.ShopCommand;
import me.trae.clans.shop.interfaces.IShopManager;
import me.trae.clans.shop.shops.armour.ArmourShopKeeper;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.core.framework.SpigotManager;

public class ShopManager extends SpigotManager<Clans> implements IShopManager {

    public ShopManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new ShopCommand(this));

        // Shop Keepers
        addModule(new ArmourShopKeeper(this));
        addModule(new ResourcesShopKeeper(this));
    }
}