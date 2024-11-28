package me.trae.clans.crate.interfaces;

import me.trae.clans.crate.loot.Loot;
import me.trae.core.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ICrate {

    ItemStack getItemStack();

    ChatColor getDisplayChatColor();

    String getDisplayName();

    String[] getDescription();

    ItemBuilder getItemBuilder();

    Loot getRandomLoot();

    void give(final Player player);
}