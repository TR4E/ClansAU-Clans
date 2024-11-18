package me.trae.clans.fields.events.interfaces;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IFieldsLootEvent {

    Material getBlockMaterial();

    List<ItemStack> getLoot();

    void setLoot(final List<ItemStack> loot);

    void addLoot(final ItemStack itemStack);

    int getMultiplier();

    void setMultiplier(final int multiplier);

    List<ItemStack> getFinalLoot();
}