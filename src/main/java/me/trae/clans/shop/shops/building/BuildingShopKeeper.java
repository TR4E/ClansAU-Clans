package me.trae.clans.shop.shops.building;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.shops.building.items.*;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilWorld;
import me.trae.core.utility.enums.DirectionType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class BuildingShopKeeper extends ShopKeeper {

    public BuildingShopKeeper(final ShopManager manager) {
        super(manager);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new BrickShopItem(this));
        addSubModule(new CobblestoneShopItem(this));
        addSubModule(new DirtShopItem(this));
        addSubModule(new NetherBrickShopItem(this));
        addSubModule(new StoneBrickShopItem(this));
        addSubModule(new StoneShopItem(this));
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Building Shop";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.VILLAGER;
    }

    @Override
    public List<Location> getLocations() {
        return Arrays.asList(
                new Location(UtilWorld.getDefaultWorld(), -425.5D, 65.0D, -6.5D, UtilLocation.getYawByDirectionType(DirectionType.EAST), 0.0F),
                new Location(UtilWorld.getDefaultWorld(), 425.5D, 65.0D, 6.5D, UtilLocation.getYawByDirectionType(DirectionType.WEST), 0.0F)
        );
    }
}