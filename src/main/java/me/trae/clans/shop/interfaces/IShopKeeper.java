package me.trae.clans.shop.interfaces;

import me.trae.clans.shop.npc.ShopNPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.List;

public interface IShopKeeper {

    String getDisplayName();

    EntityType getEntityType();

    List<Location> getLocations();

    List<ShopNPC> getNpcList();
}