package me.trae.clans.crate.interfaces;

import me.trae.clans.crate.item.CrateItemBuilder;
import me.trae.clans.crate.loot.Loot;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ICrate {

    ItemStack getItemStack();

    ChatColor getDisplayChatColor();

    String getDisplayName();

    String[] getDescription();

    CrateItemBuilder getItemBuilder();

    Loot getLootByItemStack(final ItemStack itemStack);

    boolean isLootByItemStack(final ItemStack itemStack);

    Loot getRandomLoot();

    void give(final Player player);
}