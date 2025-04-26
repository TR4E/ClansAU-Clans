package me.trae.clans.world.modules.interaction.block;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.world.modules.shared.interfaces.DisableBlockInteraction;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class DisableBedInteraction extends SpigotListener<Clans, WorldManager> implements DisableBlockInteraction {

    public DisableBedInteraction(final WorldManager manager) {
        super(manager);
    }

    @Override
    public Material getMaterial() {
        return Material.BED_BLOCK;
    }

    @Override
    public boolean isInform() {
        return false;
    }
}