package me.trae.clans.shop.interfaces;

import me.trae.clans.shop.npc.ShopKeeperNPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.List;

public interface IShopKeeper {

    String getDisplayName();

    EntityType getEntityType();

    List<Location> getLocations();

    List<ShopKeeperNPC> getNpcList();

    default void updateNPC(final ShopKeeperNPC npc, final LivingEntity entity) {
    }
}