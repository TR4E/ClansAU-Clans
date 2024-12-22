package me.trae.clans.world.enums.interfaces;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface IWarpType {

    String getName();

    int getSlot();

    ChatColor getChatColor();

    ItemStack getItemStack();

    String getDisplayName();

    Location getLocation();

    boolean showForAdministrating();
}