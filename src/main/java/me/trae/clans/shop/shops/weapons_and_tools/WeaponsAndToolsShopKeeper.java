package me.trae.clans.shop.shops.weapons_and_tools;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.npc.ShopKeeperNPC;
import me.trae.clans.shop.shops.weapons_and_tools.items.ArrowShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.BoosterBowShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.FishingRodShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.StandardBowShopItem;
import me.trae.clans.shop.shops.weapons_and_tools.items.customitems.*;
import me.trae.clans.shop.shops.weapons_and_tools.items.tools.*;
import me.trae.clans.shop.shops.weapons_and_tools.items.weapons.*;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilPlugin;
import me.trae.core.utility.UtilWorld;
import me.trae.core.utility.enums.DirectionType;
import me.trae.core.utility.enums.PluginType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class WeaponsAndToolsShopKeeper extends ShopKeeper {

    public WeaponsAndToolsShopKeeper(final ShopManager manager) {
        super(manager);
    }

    @Override
    public void registerSubModules() {
        // Custom Items
        addSubModule(new EnergyAppleShopItem(this));
        addSubModule(new EtherealPearlShopItem(this));
        if (UtilPlugin.isPluginByType(PluginType.CHAMPIONS)) {
            addSubModule(new ExtinguishingPotionShopItem(this));
            addSubModule(new GravityBombShopItem(this));
            addSubModule(new IncendiaryGrenadeShopItem(this));
            addSubModule(new ThrowingWebShopItem(this));
        }

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
        addSubModule(new FireAxeShopItem(this));
        addSubModule(new GoldAxeShopItem(this));
        addSubModule(new GoldSwordShopItem(this));
        addSubModule(new IronAxeShopItem(this));
        addSubModule(new IronSwordShopItem(this));

        // Other
        addSubModule(new ArrowShopItem(this));
        addSubModule(new StandardBowShopItem(this));
        if (UtilPlugin.isPluginByType(PluginType.CHAMPIONS)) {
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
        return EntityType.ZOMBIE;
    }

    @Override
    public List<Location> getLocations() {
        return Arrays.asList(
                new Location(UtilWorld.getDefaultWorld(), -425.5D, 65.0D, 6.5D, UtilLocation.getYawByDirectionType(DirectionType.EAST), 0.0F),
                new Location(UtilWorld.getDefaultWorld(), 425.5D, 65.0D, -6.5D, UtilLocation.getYawByDirectionType(DirectionType.WEST), 0.0F)
        );
    }

    @Override
    public void updateNPC(final ShopKeeperNPC npc, final LivingEntity entity) {
        entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
    }
}