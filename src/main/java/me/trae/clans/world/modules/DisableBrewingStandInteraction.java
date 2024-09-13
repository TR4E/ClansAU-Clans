package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.world.modules.shared.interfaces.DisableBlockInteraction;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class DisableBrewingStandInteraction extends SpigotListener<Clans, WorldManager> implements DisableBlockInteraction {

    public DisableBrewingStandInteraction(final WorldManager manager) {
        super(manager);
    }

    @Override
    public Material getMaterial() {
        return Material.BREWING_STAND;
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.BREWING;
    }

    @Override
    public boolean isInform() {
        return true;
    }
}