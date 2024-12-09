package me.trae.clans.shop.shops.weapons_and_tools;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.shops.weapons_and_tools.items.ArrowShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.BowShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.FishingRodShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.tools.*;
import me.trae.clans.shop.shops.weapons_and_tools.items.weapons.*;
import me.trae.core.utility.UtilColor;
import org.bukkit.ChatColor;

public class WeaponsAndToolsShopKeeper extends ShopKeeper {

    public WeaponsAndToolsShopKeeper(final ShopManager manager) {
        super(manager);
    }

    @Override
    public void registerSubModules() {
        // Tools
        addSubModule(new DiamondHoeShopItem(this));
        addSubModule(new DiamondPickaxeShopItem(this));
        addSubModule(new DiamondSpadeShopItem(this));
        addSubModule(new IronHoeShopItem(this));
        addSubModule(new IronPickaxeShopItem(this));
        addSubModule(new IronSpadeShopItem(this));

        // Weapons
        addSubModule(new DiamondAxeShopItem(this));
        addSubModule(new DiamondSwordShopItem(this));
        addSubModule(new GoldAxeShopItem(this));
        addSubModule(new GoldSwordShopItem(this));
        addSubModule(new IronAxeShopItem(this));
        addSubModule(new IronSwordShopItem(this));

        // Other
        addSubModule(new ArrowShopItem(this));
        addSubModule(new BowShopItem(this));
        addSubModule(new FishingRodShopItem(this));
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Weapons / Tools";
    }
}