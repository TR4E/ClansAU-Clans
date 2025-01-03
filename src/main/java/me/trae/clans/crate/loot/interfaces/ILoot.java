package me.trae.clans.crate.loot.interfaces;

import me.trae.core.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public interface ILoot {

    ItemStack getItemStack();

    double getChance();

    String getDisplayName();

    default List<String> getLore() {
        return null;
    }

    ItemBuilder getItemBuilder();

    Consumer<Player> getConsumer();

    void reward(final Player player);

    default boolean isBroadcast() {
        return false;
    }
}