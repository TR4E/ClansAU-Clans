package me.trae.clans.shop;

import me.trae.clans.Clans;
import me.trae.clans.shop.commands.ShopCommand;
import me.trae.clans.shop.interfaces.IShopManager;
import me.trae.clans.shop.modules.LoadShopKeeperNpcOnServerStart;
import me.trae.clans.shop.shops.armour.ArmourShopKeeper;
import me.trae.clans.shop.shops.building.BuildingShopKeeper;
import me.trae.clans.shop.shops.farming.FarmingShopKeeper;
import me.trae.clans.shop.shops.fishing.FishingShopKeeper;
import me.trae.clans.shop.shops.resources.ResourcesShopKeeper;
import me.trae.clans.shop.shops.traveller.TravellerMerchant;
import me.trae.clans.shop.shops.weapons_and_tools.WeaponsAndToolsShopKeeper;
import me.trae.core.framework.SpigotManager;

public class ShopManager extends SpigotManager<Clans> implements IShopManager {

    public ShopManager(final Clans instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new ShopCommand(this));

        // Modules
        addModule(new LoadShopKeeperNpcOnServerStart(this));

        // Shop Keepers
        addModule(new ArmourShopKeeper(this));
        addModule(new BuildingShopKeeper(this));
        addModule(new FarmingShopKeeper(this));
        addModule(new FishingShopKeeper(this));
        addModule(new ResourcesShopKeeper(this));
        addModule(new WeaponsAndToolsShopKeeper(this));

        // Traveller Merchant
        addModule(new TravellerMerchant(this));
    }
}