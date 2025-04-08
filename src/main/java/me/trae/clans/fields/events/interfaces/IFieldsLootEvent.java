package me.trae.clans.fields.events.interfaces;

import me.trae.clans.Clans;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.clans.worldevent.types.MiningMadness;
import me.trae.core.event.types.IPlayerEvent;
import me.trae.core.utility.UtilPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IFieldsLootEvent extends IPlayerEvent {

    Material getBlockMaterial();

    List<ItemStack> getLoot();

    void setLoot(final List<ItemStack> loot);

    void addLoot(final ItemStack itemStack);

    int getMultiplier();

    void setMultiplier(final int multiplier);

    default boolean isMiningMadness() {
        return UtilPlugin.getInstanceByClass(Clans.class).getManagerByClass(WorldEventManager.class).isActiveWorldEvent(MiningMadness.class);
    }
}