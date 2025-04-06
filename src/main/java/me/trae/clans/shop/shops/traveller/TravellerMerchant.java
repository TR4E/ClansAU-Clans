package me.trae.clans.shop.shops.traveller;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.npc.ShopKeeperNPC;
import me.trae.clans.world.WorldManager;
import me.trae.clans.world.menus.WarpMenu;
import me.trae.core.utility.*;
import me.trae.core.utility.enums.ClickType;
import me.trae.core.utility.enums.DirectionType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.Arrays;
import java.util.List;

public class TravellerMerchant extends ShopKeeper {

    public TravellerMerchant(final ShopManager manager) {
        super(manager);
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Traveller Merchant";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.VILLAGER;
    }

    @Override
    public List<Location> getLocations() {
        return Arrays.asList(
                new Location(UtilWorld.getDefaultWorld(), -1.5D, 120.0D, -484.5D, UtilLocation.getYawByDirectionType(DirectionType.NORTH), 0.0F),
                new Location(UtilWorld.getDefaultWorld(), 2.5D, 120.0D, 484.5D, UtilLocation.getYawByDirectionType(DirectionType.SOUTH), 0.0F)
        );
    }

    @Override
    public void updateNPC(final ShopKeeperNPC npc, final LivingEntity entity) {
        UtilJava.cast(Villager.class, entity).setProfession(Villager.Profession.BLACKSMITH);
    }

    @Override
    public void onClick(final Player player, final ClickType clickType) {
        UtilMenu.open(new WarpMenu(this.getInstance().getManagerByClass(WorldManager.class), player));
    }
}