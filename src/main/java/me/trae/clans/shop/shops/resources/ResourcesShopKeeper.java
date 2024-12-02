package me.trae.clans.shop.shops.resources;

import me.trae.clans.shop.ShopKeeper;
import me.trae.clans.shop.ShopManager;
import me.trae.clans.shop.shops.resources.items.DiamondShopItem;
import me.trae.clans.shop.shops.resources.items.EmeraldShopItem;
import me.trae.clans.shop.shops.resources.items.GoldIngotShopItem;
import me.trae.clans.shop.shops.resources.items.IronIngotShopItem;
import me.trae.core.utility.UtilColor;
import org.bukkit.ChatColor;

public class ResourcesShopKeeper extends ShopKeeper {

    public ResourcesShopKeeper(final ShopManager manager) {
        super(manager);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new DiamondShopItem(this));
        addSubModule(new EmeraldShopItem(this));
        addSubModule(new GoldIngotShopItem(this));
        addSubModule(new IronIngotShopItem(this));
    }

    @Override
    public String getDisplayName() {
        return UtilColor.bold(ChatColor.GREEN) + "Resources";
    }
}