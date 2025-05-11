package me.trae.clans.shop.interfaces;

import me.trae.clans.shop.ShopKeeper;
import org.bukkit.command.CommandSender;

public interface IShopManager {

    ShopKeeper searchShopKeeper(final CommandSender sender, final String name, final boolean inform);
}