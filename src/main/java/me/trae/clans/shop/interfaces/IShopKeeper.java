package me.trae.clans.shop.interfaces;

import me.trae.clans.shop.npc.ShopKeeperNPC;
import me.trae.core.utility.enums.ClickType;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public interface IShopKeeper {

    String getDisplayName();

    EntityType getEntityType();

    List<Location> getLocations();

    List<ShopKeeperNPC> getNpcList();

    void onClick(final Player player, final ClickType clickType);

    default void updateNPC(final ShopKeeperNPC npc, final LivingEntity entity) {
    }
}