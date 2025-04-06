package me.trae.clans.shop;

import me.trae.clans.Clans;
import me.trae.clans.shop.interfaces.IShopKeeper;
import me.trae.clans.shop.menus.ShopMenu;
import me.trae.clans.shop.npc.ShopKeeperNPC;
import me.trae.core.framework.SpigotModule;
import me.trae.core.utility.UtilJava;
import me.trae.core.utility.UtilMenu;
import me.trae.core.utility.UtilPlugin;
import me.trae.core.utility.enums.ClickType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class ShopKeeper extends SpigotModule<Clans, ShopManager> implements IShopKeeper {

    private final List<ShopKeeperNPC> npcList;

    public ShopKeeper(final ShopManager manager) {
        super(manager);

        this.npcList = UtilJava.createCollection(new ArrayList<>(), list -> {
            for (final Location location : this.getLocations()) {
                list.add(new ShopKeeperNPC(this.getEntityType(), location, this));
            }
        });
    }

    @Override
    public List<ShopKeeperNPC> getNpcList() {
        return this.npcList;
    }

    @Override
    public void onClick(final Player player, final ClickType clickType) {
        UtilMenu.open(new ShopMenu(this.getManager(), player, this) {
            @Override
            public ShopKeeper getShopKeeper() {
                return ShopKeeper.this;
            }
        });
    }
}