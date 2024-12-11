package me.trae.clans.shop.shops.resources;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.shops.resources.items.CompassShopItem;
import me.trae.clans.shop.shops.resources.items.LeatherShopItem;
import me.trae.clans.shop.shops.resources.items.TntShopItem;
import me.trae.clans.shop.shops.resources.items.WaterBlockShopItem;
import me.trae.clans.shop.shops.resources.items.currency.FiftyThousandDiscShopItem;
import me.trae.clans.shop.shops.resources.items.currency.HundredThousandDiscShopItem;
import me.trae.clans.shop.shops.resources.items.currency.OneMillionDiscShopItem;
import me.trae.clans.shop.shops.resources.items.gem_blocks.*;
import me.trae.clans.shop.shops.resources.items.gems.*;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilWorld;
import me.trae.core.utility.enums.DirectionType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class ResourcesShopKeeper extends ShopKeeper {

    public ResourcesShopKeeper(final ShopManager manager) {
        super(manager);
    }

    @Override
    public void registerSubModules() {
        // Currency Discs
        addSubModule(new FiftyThousandDiscShopItem(this));
        addSubModule(new HundredThousandDiscShopItem(this));
        addSubModule(new OneMillionDiscShopItem(this));

        // Gem Blocks
        addSubModule(new DiamondBlockShopItem(this));
        addSubModule(new IronBlockShopItem(this));
        addSubModule(new GoldBlockShopItem(this));
        addSubModule(new RedstoneBlockShopItem(this));
        addSubModule(new CoalBlockShopItem(this));
        addSubModule(new EmeraldBlockShopItem(this));

        // Gems
        addSubModule(new DiamondShopItem(this));
        addSubModule(new IronIngotShopItem(this));
        addSubModule(new GoldIngotShopItem(this));
        addSubModule(new RedstoneShopItem(this));
        addSubModule(new CoalShopItem(this));
        addSubModule(new EmeraldShopItem(this));

        addSubModule(new LeatherShopItem(this));
        addSubModule(new WaterBlockShopItem(this));

        addSubModule(new CompassShopItem(this));
        addSubModule(new TntShopItem(this));
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Resources Shop";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.VILLAGER;
    }

    @Override
    public List<Location> getLocations() {
        return Arrays.asList(
                new Location(UtilWorld.getDefaultWorld(), -425.5D, 65.0D, -2.5D, UtilLocation.getYawByDirectionType(DirectionType.EAST), 0.0F),
                new Location(UtilWorld.getDefaultWorld(), 425.5D, 65.0D, 2.5D, UtilLocation.getYawByDirectionType(DirectionType.WEST), 0.0F)
        );
    }
}