package me.trae.clans.shop.shops.armour;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.npc.ShopKeeperNPC;
import me.trae.clans.shop.shops.armour.items.boots.*;
import me.trae.clans.shop.shops.armour.items.chestplate.*;
import me.trae.clans.shop.shops.armour.items.helmet.*;
import me.trae.clans.shop.shops.armour.items.leggings.*;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilWorld;
import me.trae.core.utility.enums.DirectionType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ArmourShopKeeper extends ShopKeeper {

    public ArmourShopKeeper(final ShopManager manager) {
        super(manager);
    }

    @Override
    public void registerSubModules() {
        // Boots
        addSubModule(new ChainmailBootsShopItem(this));
        addSubModule(new DiamondBootsShopItem(this));
        addSubModule(new GoldBootsShopItem(this));
        addSubModule(new IronBootsShopItem(this));
        addSubModule(new LeatherBootsShopItem(this));

        // Chestplate
        addSubModule(new ChainmailChestplateShopItem(this));
        addSubModule(new DiamondChestplateShopItem(this));
        addSubModule(new GoldChestplateShopItem(this));
        addSubModule(new IronChestplateShopItem(this));
        addSubModule(new LeatherChestplateShopItem(this));

        // Helmet
        addSubModule(new ChainmailHelmetShopItem(this));
        addSubModule(new DiamondHelmetShopItem(this));
        addSubModule(new GoldHelmetShopItem(this));
        addSubModule(new IronHelmetShopItem(this));
        addSubModule(new LeatherHelmetShopItem(this));

        // Leggings
        addSubModule(new ChainmailLeggingsShopItem(this));
        addSubModule(new DiamondLeggingsShopItem(this));
        addSubModule(new GoldLeggingsShopItem(this));
        addSubModule(new IronLeggingsShopItem(this));
        addSubModule(new LeatherLeggingsShopItem(this));
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Armour Shop";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.SKELETON;
    }

    @Override
    public List<Location> getLocations() {
        return Arrays.asList(
                new Location(UtilWorld.getDefaultWorld(), -425.5D, 65.0D, 2.5D, UtilLocation.getYawByDirectionType(DirectionType.EAST), 0.0F),
                new Location(UtilWorld.getDefaultWorld(), 425.5D, 65.0D, -2.5D, UtilLocation.getYawByDirectionType(DirectionType.WEST), 0.0F)
        );
    }

    @Override
    public void updateNPC(final ShopKeeperNPC npc, final LivingEntity entity) {
        entity.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
    }
}