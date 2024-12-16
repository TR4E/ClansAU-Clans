package me.trae.clans.shop.shops.weapons_and_tools;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.shops.weapons_and_tools.items.ArrowShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.BoosterBowShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.FishingRodShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.StandardBowShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.tools.*;
import me.trae.clans.shop.shops.weapons_and_tools.items.weapons.*;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilPlugin;
import me.trae.core.utility.UtilWorld;
import me.trae.core.utility.enums.DirectionType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

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
        addSubModule(new StandardBowShopItem(this));
        if (UtilPlugin.isInstanceByName("Champions")) {
            addSubModule(new BoosterBowShopItem(this));
        }
        addSubModule(new FishingRodShopItem(this));
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Weapons / Tools Shop";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.VILLAGER;
    }

    @Override
    public List<Location> getLocations() {
        return Arrays.asList(
                new Location(UtilWorld.getDefaultWorld(), -425.5D, 65.0D, 6.5D, UtilLocation.getYawByDirectionType(DirectionType.EAST), 0.0F),
                new Location(UtilWorld.getDefaultWorld(), 425.5D, 65.0D, -6.5D, UtilLocation.getYawByDirectionType(DirectionType.WEST), 0.0F)
        );
    }
}