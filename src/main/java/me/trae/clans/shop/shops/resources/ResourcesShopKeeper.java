package me.trae.clans.shop.shops.resources;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.npc.ShopKeeperNPC;
import me.trae.clans.shop.shops.resources.items.*;
import me.trae.clans.shop.shops.resources.items.currency.FiftyThousandDiscShopItem;
import me.trae.clans.shop.shops.resources.items.currency.HundredThousandDiscShopItem;
import me.trae.clans.shop.shops.resources.items.currency.OneMillionDiscShopItem;
import me.trae.clans.shop.shops.resources.items.gem_blocks.*;
import me.trae.clans.shop.shops.resources.items.gems.*;
import me.trae.core.utility.*;
import me.trae.core.utility.enums.DirectionType;
import me.trae.core.utility.enums.PluginType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

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

        addSubModule(new LapisLazuliShopItem(this));
        addSubModule(new LeatherShopItem(this));

        if (UtilPlugin.isPluginByType(PluginType.CHAMPIONS)) {
            addSubModule(new ClassCustomizationShopItem(this));
        }

        addSubModule(new CompassShopItem(this));
        addSubModule(new TntShopItem(this));
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Resources";
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

    @Override
    public void updateNPC(final ShopKeeperNPC npc, final LivingEntity entity) {
        UtilJava.cast(Villager.class, entity).setProfession(Villager.Profession.LIBRARIAN);
    }
}