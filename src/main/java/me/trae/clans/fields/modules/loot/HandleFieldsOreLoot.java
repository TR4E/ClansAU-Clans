package me.trae.clans.fields.modules.loot;

import me.trae.clans.Clans;
import me.trae.clans.fields.FieldsManager;
import me.trae.clans.fields.events.FieldsLootEvent;
import me.trae.core.framework.types.frame.SpigotListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class HandleFieldsOreLoot extends SpigotListener<Clans, FieldsManager> {

    public HandleFieldsOreLoot(final FieldsManager manager) {
        super(manager);
    }

    @EventHandler
    public void onFieldsLoot(final FieldsLootEvent event) {
        final Material material = event.getBlockMaterial();

        if (!(material.name().endsWith("_ORE"))) {
            return;
        }

        for (int i = 0; i < event.getMultiplier(); i++) {
            switch (material) {
                case EMERALD_ORE:
                    event.addLoot(new ItemStack(Material.EMERALD));
                    break;
                case DIAMOND_ORE:
                    event.addLoot(new ItemStack(Material.DIAMOND));
                    break;
                case GOLD_ORE:
                    event.addLoot(new ItemStack(Material.GOLD_INGOT));
                    break;
                case IRON_ORE:
                    event.addLoot(new ItemStack(Material.IRON_INGOT));
                    break;
                case COAL_ORE:
                    event.addLoot(new ItemStack(Material.COAL));
                    break;
                case REDSTONE_ORE:
                case GLOWING_REDSTONE_ORE:
                    event.addLoot(new ItemStack(Material.REDSTONE));
                    break;
            }
        }
    }
}