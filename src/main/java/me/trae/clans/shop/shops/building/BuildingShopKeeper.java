package me.trae.clans.shop.shops.building;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.npc.ShopKeeperNPC;
import me.trae.clans.shop.shops.building.items.blocks.GlassShopItem;
import me.trae.clans.shop.shops.building.items.blocks.WoolShopItem;
import me.trae.clans.shop.shops.building.items.defense.NetherBrickShopItem;
import me.trae.clans.shop.shops.building.items.defense.StoneBrickShopItem;
import me.trae.clans.shop.shops.building.items.dirt.DirtShopItem;
import me.trae.clans.shop.shops.building.items.dirt.GravelShopItem;
import me.trae.clans.shop.shops.building.items.dirt.SandShopItem;
import me.trae.clans.shop.shops.building.items.redstone.PistonShopItem;
import me.trae.clans.shop.shops.building.items.redstone.StickyPistonShopItem;
import me.trae.clans.shop.shops.building.items.special.SpringBlockShopItem;
import me.trae.clans.shop.shops.building.items.special.WaterBlockShopItem;
import me.trae.clans.shop.shops.building.items.stone.CobblestoneShopItem;
import me.trae.clans.shop.shops.building.items.blocks.GlowstoneShopItem;
import me.trae.clans.shop.shops.building.items.stone.SmoothSandstoneShopItem;
import me.trae.clans.shop.shops.building.items.stone.StoneShopItem;
import me.trae.clans.shop.shops.building.items.wood.LogShopItem;
import me.trae.clans.shop.shops.building.items.wood.WoodShopItem;
import me.trae.core.utility.UtilColor;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilLocation;
import me.trae.core.utility.UtilWorld;
import me.trae.core.utility.enums.DirectionType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

import java.util.Arrays;
import java.util.List;

public class BuildingShopKeeper extends ShopKeeper {

    public BuildingShopKeeper(final ShopManager manager) {
        super(manager);
    }

    @Override
    public void registerSubModules() {
        // Blocks
        addSubModule(new GlassShopItem(this));
        addSubModule(new GlowstoneShopItem(this));
        addSubModule(new WoolShopItem(this));

        // Defense
        addSubModule(new NetherBrickShopItem(this));
        addSubModule(new StoneBrickShopItem(this));

        // Dirt
        addSubModule(new DirtShopItem(this));
        addSubModule(new GravelShopItem(this));
        addSubModule(new SandShopItem(this));

        // Redstone
        addSubModule(new PistonShopItem(this));
        addSubModule(new StickyPistonShopItem(this));

        // Special
        addSubModule(new SpringBlockShopItem(this));
        addSubModule(new WaterBlockShopItem(this));

        // Stone
        addSubModule(new CobblestoneShopItem(this));
        addSubModule(new SmoothSandstoneShopItem(this));
        addSubModule(new StoneShopItem(this));

        // Wood
        addSubModule(new LogShopItem(this));
        addSubModule(new WoodShopItem(this));
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

    @Override
    public void updateNPC(final ShopKeeperNPC npc, final LivingEntity entity) {
        UtilJava.cast(Villager.class, entity).setProfession(Villager.Profession.PRIEST);
    }
}