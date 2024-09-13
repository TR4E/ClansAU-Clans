package me.trae.clans.world.modules;

import me.trae.clans.Clans;
import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.frame.SpigotListener;
import me.trae.core.world.modules.shared.interfaces.DisableBlockInteraction;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class DisableEnchantmentTableInteraction extends SpigotListener<Clans, WorldManager> implements DisableBlockInteraction {

    public DisableEnchantmentTableInteraction(final WorldManager manager) {
        super(manager);
    }

    @Override
    public Material getMaterial() {
        return Material.ENCHANTMENT_TABLE;
    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.ENCHANTING;
    }

    @Override
    public boolean isInform() {
        return true;
    }
}