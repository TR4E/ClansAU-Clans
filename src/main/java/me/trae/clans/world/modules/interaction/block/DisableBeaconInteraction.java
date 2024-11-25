package me.trae.clans.world.modules.interaction.block;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.world.modules.shared.interfaces.DisableBlockInteraction;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class DisableBeaconInteraction extends SpigotListener<Clans, WorldManager> implements DisableBlockInteraction {

    public DisableBeaconInteraction(final WorldManager manager) {
        super(manager);
    }

    @Override
    public Material getMaterial() {
        return Material.BEACON;
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.BEACON;
    }

    @Override
    public boolean isInform() {
        return false;
    }
}