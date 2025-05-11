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
import me.trae.core.framework.Frame;
import me.trae.core.framework.SpigotManager;
import me.trae.core.utility.UtilJava;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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

    @Override
    public ShopKeeper searchShopKeeper(final CommandSender sender, final String name, final boolean inform) {
        final List<Predicate<ShopKeeper>> predicates = Arrays.asList(
                (shopKeeper -> shopKeeper.getSlicedName().equalsIgnoreCase(name)),
                (shopKeeper -> shopKeeper.getSlicedName().toLowerCase().contains(name.toLowerCase()))
        );

        final Function<ShopKeeper, String> function = Frame::getSlicedName;

        return UtilJava.search(this.getModulesByClass(ShopKeeper.class), predicates, null, function, "Shop Keeper Search", sender, name, inform);
    }
}