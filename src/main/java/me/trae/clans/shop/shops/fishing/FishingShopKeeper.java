package me.trae.clans.shop.shops.fishing;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.npc.ShopKeeperNPC;
import me.trae.clans.shop.shops.fishing.items.RawFishShopItem;
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

public class FishingShopKeeper extends ShopKeeper {

    public FishingShopKeeper(final ShopManager manager) {
        super(manager);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new RawFishShopItem(this));
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Fishing";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.VILLAGER;
    }

    @Override
    public List<Location> getLocations() {
        return Arrays.asList(
                new Location(UtilWorld.getDefaultWorld(), 406.0D, 65.0D, 9.0D, UtilLocation.getYawByDirectionType(DirectionType.NORTH), 0.0F),
                new Location(UtilWorld.getDefaultWorld(), -406.0D, 65.0D, -9.0D, UtilLocation.getYawByDirectionType(DirectionType.SOUTH), 0.0F)
        );
    }

    @Override
    public void updateNPC(final ShopKeeperNPC npc, final LivingEntity entity) {
        UtilJava.cast(Villager.class, entity).setProfession(Villager.Profession.BUTCHER);
    }
}